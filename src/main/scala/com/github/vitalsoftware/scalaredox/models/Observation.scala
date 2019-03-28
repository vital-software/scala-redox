package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives

sealed trait Observation extends DateStamped with Status {
  def Code: Option[String]
}

sealed trait ClinicalSummaryObservation extends Observation with Code

sealed trait ObservationInterpretation { self: Observation =>
  def Interpretation: Option[String]
}

sealed trait ObservationValue { self: Observation =>
  def Value: Option[String]
  def Units: Option[String]
}

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
  DateTime: DateTime,
  Status: Option[String] = None,
  TargetSite: Option[BasicCode] = None,
) extends ClinicalSummaryObservation


object ProcedureObservation extends RobustPrimitives

/**
 * Result observation
 *
 * Used within ClincialSummary's Results[].Observations[]
 */
@jsonDefaults case class ResultObservation(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  Interpretation: Option[String] = None, // Used by Result
  DateTime: DateTime,
  CodedValue: Option[BasicCode] = None, // Used by Result
  Value: Option[String] = None,
  ValueType: Option[ValueTypes.Value] = None,
  Units: Option[String] = None,
  ReferenceRange: Option[ReferenceRange] = None,
) extends ClinicalSummaryObservation with ObservationInterpretation with ObservationValue

object ResultObservation extends RobustPrimitives

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
  DateTime: DateTime,
  Value: Option[String] = None,
  Units: Option[String] = None,
) extends ClinicalSummaryObservation with ObservationInterpretation with ObservationValue

object VitalSignObservation extends RobustPrimitives

/**
 * Flowsheet observation
 *
 * Used within Flowsheet. Has a codeset instead of system. Has an observer.
 */
@jsonDefaults case class FlowsheetObservation(
  DateTime: DateTime,
  Value: Option[String] = None,
  ValueType: Option[ValueTypes.Value] = None,
  Units: Option[String] = None,
  Code: Option[String] = None,
  CodeSet: Option[String] = None,
  Description: Option[String] = None,
  Status: Option[String] = None,
  Notes: Seq[String] = Seq.empty,
  Observer: Option[Provider] = None,
  ReferenceRange: Option[ReferenceRange] = None,
  AbnormalFlag: Option[String] = None
) extends Observation with CodeSet with ObservationValue

object FlowsheetObservation extends RobustPrimitives


