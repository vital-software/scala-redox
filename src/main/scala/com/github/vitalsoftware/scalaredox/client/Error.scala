package com.github.vitalsoftware.scalaredox.client

import com.github.vitalsoftware.macros._

/**
  * Created by apatzer on 3/20/17.
  */

/**
  * Wrapper for all responses, for common functionality.
  */
case class RedoxResponse[T](result: Either[RedoxErrorResponse, T]) {
  def asOpt: Option[T] = {
    result.fold(_ => None, t => Some(t))
  }
}

@json case class RedoxErrorResponse(Errors: Seq[RedoxError] = Seq.empty)

@json case class RedoxError(ID: Long, Text: String, Type: Option[String] = None, Module: Option[String] = None)

object RedoxErrorResponse {
  val NotFound = simple("Error: JSON response not found")
  def simple(msg: String) = RedoxErrorResponse(Seq(RedoxError(0, msg)))
}

case class RedoxAuthorizationException(msg: String) extends Exception(msg)