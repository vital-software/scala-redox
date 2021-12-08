package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.{ BasicCode, CodeWithStatus }
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Procedure observation
 *
 * Used within ClinicalSummary's Procedures.Observations[]. Has no units.
 */
@jsonDefaults case class ProcedureObservation(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  DateTime: Option[String] = None,
  Status: Option[String] = None,
  TargetSite: Option[BasicCode] = None,
)

object ProcedureObservation extends RobustPrimitives

/**
 * @param Observations These are procedures that are more observational in nature, such as an EEG or EKG.
 * @param Procedures The procedure that was performed. SNOMED CT
 * @param Services These are procedures that are service-oriented in nature, such as a dressing change, or feeding a patient.
 */
@jsonDefaults case class Procedures(
  Observations: Seq[ProcedureObservation] = Seq.empty,
  Procedures: Seq[CodeWithStatus] = Seq.empty,
  Services: Seq[CodeWithStatus] = Seq.empty
)

object Procedures extends RobustPrimitives
