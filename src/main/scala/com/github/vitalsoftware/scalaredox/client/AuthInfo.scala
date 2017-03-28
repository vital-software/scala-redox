package com.github.vitalsoftware.scalaredox.client

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._

/**
  * Created by apatzer on 3/20/17.
  */
@json case class AuthInfo(accessToken: String, expires: DateTime, refreshToken: String)
