package com.github.vitalsoftware.scalaredox.client

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.Uri.Path._
import akka.stream.ActorMaterializer
import com.github.vitalsoftware.scalaredox._
import com.typesafe.config.Config
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.ws._
import play.api.libs.ws.ahc._
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.JsonBodyWritables._

import scala.concurrent.{Future, SyncVar}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scala.util.{Try, Failure, Success}

/**
  * Created by apatzer on 3/20/17.
  */
class RedoxClient(
  conf: Config,
  implicit val system: ActorSystem,
  implicit val materializer: ActorMaterializer
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

  /** Send and receive an authorized request */
  private def sendReceive[T](request: StandaloneWSRequest)(implicit format: Reads[T]): Future[RedoxResponse[T]] = {
    for {
      auth <- authInfo.get match {
        case Some(info) => Future { info }
        case None => authorize()
      }
      response <- execute[T](request.withHttpHeaders("Authorization" -> s"Bearer ${auth.accessToken}"))
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
          case Failure(e) => Left(RedoxErrorResponse.simple(r.statusText))
        }

      // Success status
      case r =>
        if (r.body.isEmpty) {
          Right(EmptyResponse .asInstanceOf[T])
        } else {
          Json.fromJson(r.body[JsValue]).fold(
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

  private def optionalQueryParam[T](el: Option[T],
                                    key: String,
                                    f: T => String = (o: T) => o.toString): Map[String, String] = {
    el.map(e => Map(key -> f(e))).getOrElse(Map.empty)
  }

  /** Authorize to Redox and save the auth tokens */
  def authorize(): Future[AuthInfo] = {
    val req = baseRequest(baseRestUri.withPath(/("auth") / "authenticate").toString())
      .withMethod("POST")
      .withBody(Map("apiKey" -> Seq(apiKey), "secret" -> Seq(apiSecret)))
    setAuth(execute[AuthInfo](req), "Cannot authenticate. Check configuration 'redox.apiKey' & 'redox.secret'")
      .map { auth =>
        scheduleRefresh(auth)
        auth
      }
  }

  /** Refresh the auth token a minute before it expires */
  protected def scheduleRefresh(auth: AuthInfo): Unit = {
    val delay = auth.expires.getMillis - DateTime.now.getMillis - 60 * 1000
    system.scheduler.scheduleOnce(delay.millis) {
      refresh(auth)
    }
  }

  /** Refresh and replace the auth token */
  def refresh(auth: AuthInfo): Future[AuthInfo] = {
    val req = baseRequest(baseRestUri.withPath(/("auth") / "refreshToken").toString())
      .withMethod("POST")
      .withBody(Map("apiKey" -> Seq(apiKey), "refreshToken" -> Seq(auth.refreshToken)))

    setAuth(sendReceive[AuthInfo](req), "Cannot refresh OAuth2 token")
  }

  /** Set a thread-safe auth-token, throwing an exception if the client authorization fails */
  private def setAuth(response: Future[RedoxResponse[AuthInfo]], errMsg: String) = {
    response.map { resp =>
      resp.result.fold(
        error => throw RedoxAuthorizationException(s"$errMsg: ${error.Errors.map(_.Text).mkString(",")}"),
        t => t
      )
    }.map { auth =>
      authInfo.take()
      authInfo.put(Some(auth))
      auth
    }
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
}

object RedoxClient {

  /**
    * Receive a webhook message and turn it into a Scala class based on the message type Meta.DataModel
    * Ex. def webhook() = Action.async(parse.json) { implicit request => RedoxClient.webhook(request.body) }
    */
  def webhook(json: JsValue): Either[JsError, AnyRef] = {
    import com.github.vitalsoftware.scalaredox.models.DataModelTypes._
    import com.github.vitalsoftware.scalaredox.models.RedoxEventTypes._
    val reads = (__ \ "Meta").read[models.Meta]
    val unsupported = JsError("Not yet supported").errors
    val clinicalSummaryTypes = List(PatientQueryResponse, PatientPush)
    val visitTypes = List(VisitQueryResponse, VisitPush)

    json.validate[models.Meta](reads).fold(
      invalid = errors => Left(errors),
      valid = { meta =>
        meta.DataModel match {
          case Claim =>               Left(unsupported)
          case Device =>              Left(unsupported)
          case Financial =>           Left(unsupported)
          case Flowsheet =>           Left(unsupported)
          case Inventory =>           Left(unsupported)
          case Media =>               json.validate[models.MediaMessage].asEither
          case Notes =>               json.validate[models.NoteMessage].asEither
          case Order =>               json.validate[models.OrderMessage].asEither
          case PatientAdmin =>        Left(unsupported)
          case PatientSearch =>       json.validate[models.PatientSearch].asEither
          case Referral =>            Left(unsupported)
          case Results =>             json.validate[models.ResultsMessage].asEither
          case Scheduling =>          Left(unsupported)
          case SurgicalScheduling =>  Left(unsupported)
          case Vaccination =>         Left(unsupported)
          case _ if clinicalSummaryTypes.contains(meta.EventType) => json.validate[models.ClinicalSummary].asEither
          case _ if visitTypes.contains(meta.EventType) => json.validate[models.Visit].asEither
          case _ =>                   Left(unsupported)
        }
      }
    ).left.map(err => JsError(err))
  }
}