package com.github.vitalsoftware.scalaredox.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.Uri.Path._
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import com.github.vitalsoftware.scalaredox.models._
import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import play.api.libs.ws._
import play.api.libs.ws.ahc._

import org.joda.time.DateTime
import scala.concurrent.{Future, SyncVar}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

/**
  * Created by apatzer on 3/20/17.
  */
class RedoxClient(conf: Config) {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  protected lazy val client = StandaloneAhcWSClient()

  private[client] lazy val apiKey = conf.as[String]("redox.apiKey")
  private[client] lazy val apiSecret = conf.as[String]("redox.secret")
  private[client] lazy val baseRestUri = Uri(conf.getOrElse[String]("redox.restApiBase", "https://api.redoxengine.com/"))
  private[client] lazy val authInfo = {
    val auth = new SyncVar[Option[AuthInfo]]
    auth.put(None)
    auth
  }

  private def baseRequest(url: String) = client.url(url).withHeaders("apiKey" -> apiKey, OAuth2BearerToken("").params.toSeq:_*)
  private def baseQuery = baseRequest(baseRestUri.withPath(/("query")).toString()).withMethod("GET")
  private def basePost = baseRequest(baseRestUri.withPath(/("endpoint")).toString())
    .withMethod("POST").withHeaders(ContentTypes.`application/json`)

  private def sendReceive[T](request: StandaloneWSRequest): Future[RedoxResponse[T]] = {
    for {
      auth <- authInfo.get match {
        case Some(info) => Future { info }
        case None => authorize()
      }
      response <- request.withHeaders("accessToken" -> auth.accessToken).execute().map {
        case r if Set[StatusCode](
          BadRequest,
          Unauthorized,
          Forbidden,
          NotFound,
          MethodNotAllowed,
          RequestedRangeNotSatisfiable
        ).contains(r.status) => r.json.asOpt[RedoxErrorResponse].map(Left(_))
        case r => r.json.asOpt[T].map(Right(_))
      }
    } yield {
      RedoxResponse[T](response.getOrElse(Left(RedoxErrorResponse.NotFound)))
    }
  }

  private def optionalQueryParam[T](el: Option[T],
                                    key: String,
                                    f: T => String = (o: T) => o.toString): Map[String, String] = {
    el.map(e => Map(key -> f(e))).getOrElse(Map.empty)
  }

  protected def refresh(auth: AuthInfo): Future[AuthInfo] = {
    val delay = auth.expires.getMillis - DateTime.now.getMillis
    system.scheduler.scheduleOnce(delay.millis) {
      val req = baseRequest(baseRestUri.withPath(/("auth/refreshToken")).toString())
        .withMethod("POST")
        .withHeaders("refreshToken" -> auth.refreshToken)

      setAuth(sendReceive[AuthInfo](req), "Cannot refresh OAuth2 token")
    }
  }

  protected def authorize(): Future[AuthInfo] = {
    val req = baseRequest(baseRestUri.withPath(/("auth/authenticate")).toString()).withMethod("POST")
    setAuth(sendReceive[AuthInfo](req), "Cannot authenticate. Check configuration 'redox.apiKey' & 'redox.secret'")
  }

  private def setAuth(response: Future[RedoxResponse[AuthInfo]], errMsg: String) = {
    response.map {
      case RedoxResponse(result) => result.fold(
        error => throw RedoxClientException(errMsg),
        t => t
      )
    }.map { auth =>
      authInfo.put(Some(auth))
      auth
    }
  }

  def getClinicalSummary: Future[RedoxResponse[ClinicalSummaryQuery]] = {
    sendReceive[ClinicalSummaryQuery](baseQuery)
  }

}