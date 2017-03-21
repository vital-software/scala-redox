package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.macros._

/**
  * Created by apatzer on 3/20/17.
  */

/**
  * @param DateTime The date and time of the reading. ISO 8601 Format
  * @param Observations The type of vital sign being read (height, weight, blood pressure, etc.). Subset of LOINC codes (HITSP Vital Sign Result Type).
  */
@json case class VitalSigns(
  DateTime: DateTime,
  Observations: Seq[Observation] = Seq.empty
)

/**
  * This sections contains all vital sign readings for a patient recorded over time.
  *
  * @param VitalSignsText Free text form of the vital signs summary
  * @param VitalSigns An array of groups of vital signs. Each element represents one time period in which vitals were recorded.
  */
@json case class VitalSignsMessage(
  VitalSignsText: Option[String] = None,
  VitalSigns: Seq[VitalSigns] = Seq.empty
)
