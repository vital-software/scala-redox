package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/17/17.
  */


case class Visit( // Todo: Check what's required
  Location: CareLocation,
  StartDateTime: DateTime,
  Reason: String, // The reason for visit.
  EndDateTime: DateTime
)

