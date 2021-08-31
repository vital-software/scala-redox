package com.github.vitalsoftware.scalaredox.client

import java.io.File
import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.Materializer
import akka.stream.scaladsl.{ FileIO, Source }
import com.github.vitalsoftware.scalaredox._
import com.github.vitalsoftware.scalaredox.models.{ RedoxEventTypes, Upload }
import com.github.vitalsoftware.scalaredox.models.clinicalsummary.{ PatientPush, PatientQueryResponse }
import com.github.vitalsoftware.util.JsonImplicits.JsValueExtensions
import com.github.vitalsoftware.util.RobustParsing
import com.typesafe.config.Config
import play.api.libs.json._
import play.api.libs.ws._
import play.api.libs.ws.ahc._
import play.api.mvc.MultipartFormData.FilePart

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.language.implicitConversions

class RedoxClient(
  conf: ClientConfig,
  client: HttpClient,
  tokenManager: RedoxTokenManager,
  reducer: JsValue => JsValue = _.reduceNullSubtrees
)(
  implicit val system: ActorSystem,
  implicit val materializer: Materializer,
) extends RedoxClientComponents(client, conf.baseRestUri, reducer) {
  /**
   * Initialize and internal HttpClient and a token manager. Added for backward compatibility.
   * @deprecated prefer initializing the HttpClient and token manager outside RedoxClient. Do not create
   *             more than one RedoxClient with this constructor as it will initialize duplicate
   *             TokenManagers and http clients.
   */
  def this(
    conf: Config,
    client: HttpClient,
    reducer: JsValue => JsValue
  )(implicit system: ActorSystem, materializer: Materializer) = {
    this(conf, client, new RedoxTokenManager(client, ClientConfig(conf).baseRestUri), reducer)
  }

  /**
   * Use default reducer, with internally initialized http client and token manager.
   *
   * @deprecated prefer initializing the HttpClient and token manager outside RedoxClient. Do not create
   *             more than one RedoxClient with this constructor as it will initialize duplicate
   *             TokenManagers and http clients.
   */
  def this(
    conf: Config
  )(implicit system: ActorSystem, materializer: Materializer) {
    this(conf, StandaloneAhcWSClient(), _.reduceEmptySubtrees)
  }

  /** Send and receive an authorized request */
  private def sendReceive[T](request: StandaloneWSRequest)(implicit format: Reads[T]): Future[RedoxResponse[T]] =
    for {
      auth <- tokenManager.getAccessToken(conf.apiKey, conf.apiSecret)
      response <- execute[T](request.addHttpHeaders("Authorization" -> s"Bearer ${auth.accessToken}"))
    } yield response

  private def optionalQueryParam[T](
    el: Option[T],
    key: String,
    f: T => String = (o: T) => o.toString
  ): Map[String, String] =
    el.map(e => Map(key -> f(e))).getOrElse(Map.empty)

  /**
   * Send a query/read-request of type 'T' expecting a response of type 'U'
   * Ex. get[PatientQuery => case  ClinicalSummary](query)
   */
  def get[T, U](query: T)(implicit writes: Writes[T], reads: Reads[U]): Future[RedoxResponse[U]] =
    sendReceive[U](baseQuery.withBody(Json.toJson(query)))

  /**
   * Send a post/write-request of type 'T' expecting a response of type 'U'
   * Ex. post[ClinicalSummary => case  EmptyResponse](data)
   */
  def post[T, U](data: T)(implicit writes: Writes[T], reads: Reads[U]): Future[RedoxResponse[U]] =
    sendReceive[U](basePost.withBody(Json.toJson(data)))

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
  def upload(
    file: File,
    fileContentType: String = "text/plain",
    contentLength: Long = 2097000L
  ): Future[RedoxResponse[Upload]] =
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
    val visitTypes = List(VisitQueryResponse, VisitPush)

    json.validate[models.Meta](reads).asEither.flatMap { meta =>
      (meta.DataModel, meta.EventType) match {
        case (Order, GroupedOrders)                                  => Right(implicitly[Reads[models.GroupedOrdersMessage]])
        case (Order, _)                                              => Right(implicitly[Reads[models.OrderMessage]])
        case (Claim, _)                                              => Left(unsupported)
        case (Device, _)                                             => Left(unsupported)
        case (Financial, _)                                          => Left(unsupported)
        case (Flowsheet, _)                                          => Right(implicitly[Reads[models.FlowSheetMessage]])
        case (Inventory, _)                                          => Left(unsupported)
        case (Media, _)                                              => Right(implicitly[Reads[models.MediaMessage]])
        case (Notes, _)                                              => Right(implicitly[Reads[models.NoteMessage]])
        case (PatientAdmin, _)                                       => Right(implicitly[Reads[models.PatientAdminMessage]])
        case (PatientSearch, _)                                      => Right(implicitly[Reads[models.PatientSearch]])
        case (Referral, _)                                           => Left(unsupported)
        case (Results, _)                                            => Right(implicitly[Reads[models.ResultsMessage]])
        case (Scheduling, _)                                         => Left(unsupported)
        case (SurgicalScheduling, _)                                 => Left(unsupported)
        case (Vaccination, _)                                        => Left(unsupported)
        case (Medications, _)                                        => Right(implicitly[Reads[models.MedicationsMessage]])
        case (ClinicalSummary, RedoxEventTypes.PatientQueryResponse) => Right(implicitly[Reads[PatientQueryResponse]])
        case (ClinicalSummary, RedoxEventTypes.PatientPush)          => Right(implicitly[Reads[PatientPush]])
        case _ if visitTypes.contains(meta.EventType)                => Right(implicitly[Reads[models.Visit]])
        case _                                                       => Left(unsupported)
      }
    } match {
      case Left(error)  => (Some(JsError(error)), None)
      case Right(reads) => RobustParsing.robustParsing(reads, reducer(json))
    }
  }
}

case class ClientConfig(baseRestUri: Uri, apiKey: String, apiSecret: String)
object ClientConfig {
  implicit def apply(conf: Config): ClientConfig = {
    val apiKey = conf.getString("redox.apiKey")
    val apiSecret = conf.getString("redox.secret")
    val baseRestUri = Uri(conf.getString("redox.restApiBase"))

    ClientConfig(baseRestUri, apiKey, apiSecret)
  }
}
