package com.github.vitalsoftware.scalaredox.client

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.Uri.Path._
import akka.http.scaladsl.model._
import play.api.libs.json._
import play.api.libs.ws._

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success, Try }

abstract class RedoxClientComponents(
  client: HttpClient,
  baseRestUri: Uri,
  reducer: JsValue => JsValue
)(
  implicit
  ec: ExecutionContext
) {
  protected def baseRequest(url: String) = client.url(url)
  protected def baseQuery = baseRequest(baseRestUri.withPath(/("query")).toString()).withMethod("POST")
  protected def basePost = baseRequest(baseRestUri.withPath(/("endpoint")).toString()).withMethod("POST")
  protected def baseUpload = baseRequest(baseRestUri.withPath(/("upload")).toString()).withMethod("POST")

  /** Raw request execution */
  protected def execute[T](request: StandaloneWSRequest)(implicit format: Reads[T]): Future[RedoxResponse[T]] =
    request
      .execute()
      .map {
        // Failure status
        case r
            if Set[StatusCode](
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

            Json
              .fromJson(json)
              .fold(
                // Json to Scala objects failed...force into RedoxError format
                invalid = err => Left(RedoxErrorResponse.fromJsError(JsError(err))),
                // All good
                valid = t => Right(t)
              )
          }
      }
      .map { response =>
        RedoxResponse[T](response)
      }
}
