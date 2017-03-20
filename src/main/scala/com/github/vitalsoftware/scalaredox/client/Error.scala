package com.github.vitalsoftware.scalaredox.client

import com.kifi.macros._

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

@json case class RedoxErrorResponse(Error: String)

object RedoxErrorResponse {
  val NotFound = RedoxErrorResponse("Not found")
}