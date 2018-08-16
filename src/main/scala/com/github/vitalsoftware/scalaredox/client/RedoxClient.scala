package com.github.vitalsoftware.scalaredox.client

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.Uri.Path._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ FileIO, Source }
import com.github.vitalsoftware.scalaredox._
import com.github.vitalsoftware.scalaredox.models.{ MediaMessage, Upload }
import com.github.vitalsoftware.util.JsonImplicits.JsValueExtensions
import com.typesafe.config.Config
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws._
import play.api.libs.ws.ahc._
import play.api.mvc.MultipartFormData.FilePart

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._
import scala.concurrent.{ Future, SyncVar }
import scala.util.{ Failure, Success, Try }

/**
 * Created by apatzer on 3/20/17.
 */
class RedoxClient(
  conf: Config,
  implicit val system: ActorSystem,
  implicit val materializer: ActorMaterializer,
  reducer: JsValue => JsValue = _.reduceNullSubtrees
) {

  private val client = StandaloneAhcWSClient()

  private[client] val apiKey = conf.getString("redox.apiKey")
  private[client] val apiSecret = conf.getString("redox.secret")
  private[client] val baseRestUri = Uri(conf.getString("redox.restApiBase"))
  private[client] lazy val authInfo = {
    val auth = new SyncVar[Option[AuthInfo]]
    auth.put(None)
    auth
  }

  private def baseRequest(url: String) = client.url(url)
  private def baseQuery = baseRequest(baseRestUri.withPath(/("query")).toString()).withMethod("POST")
  private def basePost = baseRequest(baseRestUri.withPath(/("endpoint")).toString()).withMethod("POST")
  private def baseUpload = baseRequest(baseRestUri.withPath(/("upload")).toString()).withMethod("POST")

  /** Send and receive an authorized request */
  private def sendReceive[T](request: StandaloneWSRequest)(implicit format: Reads[T]): Future[RedoxResponse[T]] = {
    for {
      auth <- authInfo.get match {
        case Some(info) => Future.successful(info)
        case None       => setAuthAndScheduleRefresh(authorize())
      }
      response <- execute[T](request.addHttpHeaders("Authorization" -> s"Bearer ${auth.accessToken}"))
    } yield response
  }

  /** Raw request execution */
  private def execute[T](request: StandaloneWSRequest)(implicit format: Reads[T]): Future[RedoxResponse[T]] = {
    request.execute().map {

      // Failure status
      case r if Set[StatusCode](
        BadRequest,
        Unauthorized,
        Forbidden,
        NotFound,
        MethodNotAllowed,
        RequestedRangeNotSatisfiable
      ).contains(r.status) =>
        Try {
          // In case we do not get valid JSON back, wrap everything in a Try block
          (r.body[JsValue] \ "Meta").as[RedoxErrorResponse]
        } match {
          case Success(t) => Left(t)
          case Failure(e) => Left(RedoxErrorResponse.simple(r.statusText, r.body))
        }

      // Success status
      case r =>
        if (r.body.isEmpty) {
          Right(EmptyResponse.asInstanceOf[T])
        } else {
          val json = reducer(r.body[JsValue])

          Json.fromJson(json).fold(
            // Json to Scala objects failed...force into RedoxError format
            invalid = err => Left(RedoxErrorResponse.fromJsError(JsError(err))),

            // All good
            valid = t => Right(t)
          )
        }
    }.map { response =>
      RedoxResponse[T](response)
    }
  }

  private def optionalQueryParam[T](
    el: Option[T],
    key: String,
    f: T => String = (o: T) => o.toString
  ): Map[String, String] = {
    el.map(e => Map(key -> f(e))).getOrElse(Map.empty)
  }

  /** Authorize to Redox, returning a Future containing the access and refresh tokens. */
  def authorize(): Future[AuthInfo] = {
    val req = baseRequest(baseRestUri.withPath(/("auth") / "authenticate").toString())
      .withMethod("POST")
      .withBody(Json.toJson(AuthRequest(apiKey = apiKey, secret = apiSecret)))
    execute[AuthInfo](req).map { result =>
      parseAuthResponse(result, "Cannot authenticate. Check configuration 'redox.apiKey' & 'redox.secret'")
    }
  }

  /**
   * Refresh the auth token a minute before it expires. Set and schedule a new refresh to occur.
   * NOTE: If this method is overridden, scheduling and storing the new auth token will
   * not be available to the implementing class.
   */
  protected def scheduleRefresh(auth: AuthInfo): Unit = {
    val delay = auth.expires.getMillis - DateTime.now.getMillis - 60 * 1000
    system.scheduler.scheduleOnce(delay.millis) {
      setAuthAndScheduleRefresh(refresh(auth))
    }
  }

  /** Refresh the access and refresh tokens. */
  def refresh(auth: AuthInfo): Future[AuthInfo] = {
    val req = baseRequest(baseRestUri.withPath(/("auth") / "refreshToken").toString())
      .withMethod("POST")
      .withBody(Json.toJson(RefreshRequest(apiKey = apiKey, refreshToken = auth.refreshToken)))
    sendReceive[AuthInfo](req).map(result => parseAuthResponse(result, "Cannot refresh OAuth2 token"))
  }

  /** Set a thread-safe auth-token, and schedule a refresh. */
  private def setAuthAndScheduleRefresh(authFuture: Future[AuthInfo]): Future[AuthInfo] = {
    authFuture
      .map { auth =>
        authInfo.take()
        authInfo.put(Some(auth))
        auth
      }
      .map { auth =>
        scheduleRefresh(auth)
        auth
      }
  }

  /** Parse the result of an auth request, either returning a new AuthInfo object, or throwing an error. */
  private def parseAuthResponse(authResult: RedoxResponse[AuthInfo], errMsg: String) = {
    authResult.result.fold(
      error => throw RedoxAuthorizationException(s"$errMsg: ${error.Errors.map(_.Text).mkString(",")}"),
      identity
    )
  }

  /**
   * Send a query/read-request of type 'T' expecting a response of type 'U'
   * Ex. get[PatientQuery => case  ClinicalSummary](query)
   */
  def get[T, U](query: T)(implicit writes: Writes[T], reads: Reads[U]): Future[RedoxResponse[U]] = {
    sendReceive[U](baseQuery.withBody(Json.toJson(query)))
  }

  /**
   * Send a post/write-request of type 'T' expecting a response of type 'U'
   * Ex. post[ClinicalSummary => case  EmptyResponse](data)
   */
  def post[T, U](data: T)(implicit writes: Writes[T], reads: Reads[U]): Future[RedoxResponse[U]] = {
    sendReceive[U](basePost.withBody(Json.toJson(data)))
  }

  /**
   * Uploads a file to the Redox /uploads endpoint. Provides sensible defaults for
   * file content type and content length.
   *
   * @param file The file to upload to Redox.
   * @param fileContentType The content type of the file (used when converting the file to a source stream.
   *                        Defaults to 'text/plain'.
   * @param contentLength The length that will be used to set the 'content-length' on the request.
   *                      Provides a default value of of ~2MB (2,097,000 octets). This is needed as when we provide
   *                      a Source we have to manually set the length or the backend http client will chunk the
   *                      request. (src: play.api.libs.ws.ahc.StandaloneAhcWSRequest#buildRequest() line: 303).
   * @return A future containing the redox upload response.
   */
  def upload(file: File, fileContentType: String = "text/plain", contentLength: Long = 2097000L): Future[RedoxResponse[Upload]] = {
    sendReceive[Upload] {
      baseUpload
        .withBody(
          Source(
            FilePart(
              key = "file",
              filename = file.getName,
              contentType = Some(fileContentType),
              ref = FileIO.fromPath(file.toPath)
            ) :: List()
          )
        )
        .addHttpHeaders("Content-Length" -> contentLength.toString)
    }
  }
}

object RedoxClient {
  /**
   * Receive a webhook message and turn it into a Scala class based on the message type Meta.DataModel
   * Ex. def webhook() = Action.async(parse.json) { implicit request => RedoxClient.webhook(request.body) }
   * Since this do robust parsing, The result may contain errors. Which means that with results, there may
   * or may not be errors.
   */
  def webhook(json: JsValue, reducer: JsValue => JsValue = identity): (Option[JsError], Option[AnyRef]) = {
    import com.github.vitalsoftware.scalaredox.models.DataModelTypes._
    import com.github.vitalsoftware.scalaredox.models.RedoxEventTypes._
    val reads = (__ \ "Meta").read[models.Meta]
    val unsupported = JsError("Not yet supported").errors
    val clinicalSummaryTypes = List(PatientQueryResponse, PatientPush)
    val visitTypes = List(VisitQueryResponse, VisitPush)

    json.validate[models.Meta](reads).asEither.flatMap { meta =>
      (meta.DataModel, meta.EventType) match {
        case (Order, GroupedOrders) => Right(implicitly[Reads[models.GroupedOrdersMessage]])
        case (Order, _) => Right(implicitly[Reads[models.OrderMessage]])
        case (Claim, _) => Left(unsupported)
        case (Device, _) => Left(unsupported)
        case (Financial, _) => Left(unsupported)
        case (Flowsheet, _) => Right(implicitly[Reads[models.FlowSheetMessage]])
        case (Inventory, _) => Left(unsupported)
        case (Media, _) => Right(implicitly[Reads[models.MediaMessage]])
        case (Notes, _) => Right(implicitly[Reads[models.NoteMessage]])
        case (PatientAdmin, _) => Right(implicitly[Reads[models.PatientAdminMessage]])
        case (PatientSearch, _) => Right(implicitly[Reads[models.PatientSearch]])
        case (Referral, _) => Left(unsupported)
        case (Results, _) => Right(implicitly[Reads[models.ResultsMessage]])
        case (Scheduling, _) => Left(unsupported)
        case (SurgicalScheduling, _) => Left(unsupported)
        case (Vaccination, _) => Left(unsupported)
        case _ if clinicalSummaryTypes.contains(meta.EventType) => Right(implicitly[Reads[models.ClinicalSummary]])
        case _ if visitTypes.contains(meta.EventType) => Right(implicitly[Reads[models.Visit]])
        case _ => Left(unsupported)
      }
    } match {
      case Left(error)  => (Some(JsError(error)), None)
      case Right(reads) => robustParsing(reads, reducer(json))
    }
  }

  def robustParsing[A](reads: Reads[A], json: JsValue): (Option[JsError], Option[A]) = json.validate(reads)
    .fold(
      invalid = { errors =>
        val transforms = errors.map(_._1)
          .map(_.json.prune)
          .reduce(_ andThen _)

        json.transform(transforms)
          .flatMap(_.validate(reads))
          .map(res => (Some(JsError(errors)), Option(res)))
          .recoverTotal(_ => (Some(JsError(errors)), None))
      },
      valid = res => (None, Some(res))
    )
}
