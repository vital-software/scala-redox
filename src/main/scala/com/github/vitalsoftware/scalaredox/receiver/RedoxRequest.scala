package com.github.vitalsoftware.scalaredox.receiver

import com.google.common.base.MoreObjects
import play.api.libs.json.JsValue
import play.api.mvc.Request
import com.github.vitalsoftware.scalaredox.receiver.RedoxRequest.TokenHeader

case class RedoxRequest(underlying: Request[JsValue]) {
  override def toString: String = MoreObjects.toStringHelper(this)
    .add("host", underlying.host)
    .add("remoteAddress", underlying.remoteAddress)
    .add("method", underlying.method)
    .add("path", underlying.path)
    .add("tokenHeader", token.getOrElse("unspecified"))
    .toString

  val token: Option[String] = underlying.headers.get(TokenHeader)
}

object RedoxRequest {
  final val TokenHeader: String = "verification-token"
}
