package com.github.vitalsoftware.scalaredox.client

import com.github.vitalsoftware.macros._
import play.api.libs.json.JsError

/**
 * Wrapper for all responses, for common functionality.
 */
case class RedoxResponse[T](result: Either[RedoxErrorResponse, T]) {
  def asOpt: Option[T] =
    result.fold(_ => None, t => Some(t))
  def get: T = result.right.get
  def getError: RedoxErrorResponse = result.left.get
  def isError: Boolean = result.isLeft
  def isSuccess: Boolean = !isError
}

@jsonDefaults case class EmptyResponse(none: Option[String] = None)

@json case class RedoxError(ID: Long, Text: String, Type: Option[String] = None, Module: Option[String] = None)

@json case class RedoxErrorResponse(Errors: Seq[RedoxError] = Seq.empty)

object RedoxErrorResponse {
  val NotFound = simple("Error: JSON response not found")
  def simple(msgs: String*) = RedoxErrorResponse(msgs.zipWithIndex.map(msg => RedoxError(msg._2, msg._1)))
  def fromJsError(jsError: JsError) = simple(JsError.toJson(jsError).toString())
}

case class RedoxAuthorizationException(msg: String) extends Exception(msg)
