package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.kifi.macros._

/**
  * Created by apatzer on 3/17/17.
  */


@json case class Visit( // Todo: Check what's required
  Location: CareLocation,
  StartDateTime: DateTime,
  EndDateTime: DateTime,
  Reason: String
  //AttendingProvider: Option[Provider] = None
  //ReferringProvider: Option[Provider] = None
)
