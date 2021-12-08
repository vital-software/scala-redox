package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.{ BasicCode, ChartResultProducer, Code, Status, ValueTypes }
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Reference range for the result.
 * Numeric result values will use the low and high properties.
 * Non-numeric result values will put the normal value in the text property.
 *
 * @param Low Lower bound for a normal result
 * @param High Upper bound for a normal result
 * @param Text The normal value for non-numeric results
 */
@jsonDefaults case class ReferenceRange(
  Low: Option[String] = None,
  High: Option[String] = None,
  Text: Option[String] = None
)

object ReferenceRange extends RobustPrimitives

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
  AltCodes: Option[Seq[BasicCode]] = None,
  Status: Option[String] = None,
  Interpretation: Option[String] = None, // Used by Result
  DateTime: Option[String] = None,
  CodedValue: Option[BasicCode] = None, // Used by Result
  Value: Option[String] = None,
  ValueType: Option[ValueTypes.Value] = None,
  Units: Option[String] = None,
  ReferenceRange: Option[ReferenceRange] = None,
)

object ResultObservation extends RobustPrimitives

/**
 * Result from laboratories, imaging procedures, and other procedures.
 *
 * @param Code The test performed and resulted. LOINC for Lab - SNOMED CT otherwise
 * @param Status The status of the test (In Progress, Final)
 * @param Observations A list of corresponding observations for the test (result components)
 */
@jsonDefaults case class ChartResult(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  Producer: Option[ChartResultProducer] = None,
  Observations: Seq[ResultObservation] = Seq.empty
) extends Code
    with Status

object ChartResult extends RobustPrimitives
