package com.github.vitalsoftware.scalaredox.client

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._

@json case class AuthRequest(apiKey: String, secret: String)

@json case class RefreshRequest(apiKey: String, refreshToken: String)

@json case class AuthInfo(accessToken: String, expires: DateTime, refreshToken: String)
