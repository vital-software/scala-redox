package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Vital sign observation
 *
 * Used within ClinicalSummary's VitalSigns[].Observations[]
 */
@jsonDefaults case class VitalSignObservation(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  Interpretation: Option[String] = None,
  DateTime: Option[String] = None,
  Value: Option[String] = None,
  Units: Option[String] = None,
)

object VitalSignObservation extends RobustPrimitives

/**
 * @param DateTime The date and time of the reading. ISO 8601 Format
 * @param Observations The type of vital sign being read (height, weight, blood pressure, etc.).
 *                     Subset of LOINC codes (HITSP Vital Sign Result Type).
 */
@jsonDefaults case class VitalSigns(
  DateTime: Option[String] = None,
  Observations: Seq[VitalSignObservation] = Seq.empty
)

object VitalSigns extends RobustPrimitives
