package com.github.vitalsoftware.scalaredox.client

import org.joda.time.DateTime
import com.github.vitalsoftware.scalaredox.util.JsonImplicits._
import com.github.vitalsoftware.macros._

/**
  * Created by apatzer on 3/20/17.
  */
@json case class AuthInfo(accessToken: String, expires: DateTime, refreshToken: String)
