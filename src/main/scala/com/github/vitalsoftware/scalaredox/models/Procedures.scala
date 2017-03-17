package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * This section documents three types of things: diagnostic procedures, procedures that change the body, and services performed by clinical staff.
  *
  * @param ProceduresText Free text form of the procedures summary
  * @param Procedures A general grouper for all things that CDA considers procedures.
  */
case class ProceduresMessage(
  ProceduresText: Option[String] = None,
  Procedures: Procedures
)

/**
  *
  * @param Observations These are procedures that are more observational in nature, such as an EEG or EKG.
  * @param Procedures The procedure that was performed. SNOMED CT
  * @param Services These are procedures that are service-oriented in nature, such as a dressing change, or feeding a patient.
  */
case class Procedures(
  Observations: Seq[Observation] = Seq.empty,
  Procedures: Seq[CodeWithStatus] = Seq.empty,
  Services: Seq[CodeWithStatus] = Seq.empty
)

/**
  * Coded Observation of a patient.
  *
  * @param TargetSite Where (on or in the body) the observation is made. (e.g. "Entire hand (body structure)"). SNOMED CT
  * @param Interpretation A flag indicating whether or not the observed value is normal, high, or low. [Supported Values](https://www.hl7.org/fhir/v3/ObservationInterpretation/index.html)
  */
case class Observation(
  Code: String,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  DateTime: Option[DateTime] = None,
  TargetSite: Option[BasicCode] = None,  // Used by Procedures
  Interpretation: Option[String] = None, // Used by Result
  Value: Option[String] = None,
  Units: Option[String] = None,
  ReferenceRange: Option[ReferenceRange] = None
) extends Code with Status with DateStamped

/**
  * Reference range for the result.
  * Numeric result values will use the low and high properties.
  * Non-numeric result values will put the normal value in the text property.
  *
  * @param Low Lower bound for a normal result
  * @param High Upper bound for a normal result
  * @param Text The normal value for non-numeric results
  */
case class ReferenceRange(
  Low: Option[Double] = None,
  High: Option[Double] = None,
  Text: Option[String] = None
)
