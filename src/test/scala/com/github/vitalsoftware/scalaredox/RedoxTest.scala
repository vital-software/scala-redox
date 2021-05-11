package com.github.vitalsoftware.scalaredox

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.github.vitalsoftware.scalaredox.client.{ClientConfig, RedoxClient, RedoxResponse, RedoxTokenManager}
import com.github.vitalsoftware.util.JsonImplicits.JsValueExtensions
import com.typesafe.config.ConfigFactory
import play.api.libs.json.{JsError, Json, Reads}
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
 * Created by apatzer on 3/23/17.
 */
trait RedoxTest {
  implicit val system = ActorSystem("redox-test")
  implicit val materializer = ActorMaterializer()(system)
  val conf = ClientConfig(ConfigFactory.load("resources/reference.conf"))
  val httpClient = StandaloneAhcWSClient()

  val tokenManager = new RedoxTokenManager(httpClient, conf.baseRestUri)
  val client = new RedoxClient(conf, httpClient, tokenManager)
  val timeout: FiniteDuration = 20.seconds

  // Validate raw a raw JSON string, throwing a RuntimeException which will output detail to Specs2/Test console
  def validateJsonInput[T](json: String)(implicit reads: Reads[T]): T = {
    Json
      .parse(json)
      .validate[T]
      .fold(
        invalid = err => throw new RuntimeException(JsError.toJson(JsError(err)).toString()),
        valid = identity
      )

    Json
      .fromJson[T](Json.parse(json).reduceNullSubtrees)
      .fold(
        invalid = err => throw new RuntimeException(JsError.toJson(JsError(err)).toString()),
        valid = identity
      )
  }

  def handleResponse[T](fut: Future[RedoxResponse[T]]): Option[T] = {
    val resp = Await.result(fut, timeout)
    if (resp.isError) throw new RuntimeException(resp.getError.Errors.map(_.Text).mkString(", "))
    resp.asOpt
  }
}
