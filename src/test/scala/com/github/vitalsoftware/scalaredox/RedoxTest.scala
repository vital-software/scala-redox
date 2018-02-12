package com.github.vitalsoftware.scalaredox

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.github.vitalsoftware.scalaredox.client.{ RedoxClient, RedoxResponse }
import com.github.vitalsoftware.util.JsonImplicits.JsValueExtensions
import com.typesafe.config.ConfigFactory
import play.api.libs.json.{ JsError, Json, Reads }

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

/**
 * Created by apatzer on 3/23/17.
 */
trait RedoxTest {

  val conf = ConfigFactory.load("resources/reference.conf")
  val system = ActorSystem("redox-test")
  val materializer = ActorMaterializer()(system)
  val client = new RedoxClient(conf, system, materializer)
  val timeout: FiniteDuration = 20.seconds

  // Validate raw a raw JSON string, throwing a RuntimeException which will output detail to Specs2/Test console
  def validateJsonInput[T](json: String)(implicit reads: Reads[T]): T = {
    val jsValue = Json.parse(json).reduceNullSubtrees
    Json.parse(json).validate[T].fold(
      invalid = err => throw new RuntimeException(JsError.toJson(JsError(err)).toString()),
      valid = identity
    )
    Json.fromJson[T](jsValue).get
  }

  def handleResponse[T](fut: Future[RedoxResponse[T]]): Option[T] = {
    val resp = Await.result(fut, timeout)
    if (resp.isError) throw new RuntimeException(resp.getError.Errors.map(_.Text).mkString(", "))
    resp.asOpt
  }
}
