package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * This section lists the patient's past encounters at the health system and associated diagnoses.
  *
  * @param EncountersText Free text form of the encounters summary
  * @param Encounters Patient encounters
  */
case class EncountersMessage(
  EncountersText: Option[String],
  Encounters: Seq[Encounter] = Seq.empty
)

/**
  *
  * @param Type A code describing the type of encounter (office visit, hospital, etc). CPT-4
  * @param DateTime When the encounter took place, or alternatively when the encounter began if Encounters[].EndDateTime is present. ISO 8601 Format
  * @param EndDateTime When the encounter was completed, if available. ISO 8601 Format
  * @param Providers Providers seen
  * @param Locations The type of location where the patient was seen (Clinic, Urgent Care, Hostpital).
  * @param Diagnosis List of Diagnoses associated with the visit. SNOMED CT
  * @param ReasonForVisit The reason for the visit (usually this is what the patient reports). SNOMED CT
  */
case class Encounter(
  Type: BasicCode,
  DateTime: DateTime,
  EndDateTime: Option[DateTime] = None,
  Providers: Seq[Provider] = Seq.empty,
  Locations: Seq[Location] = Seq.empty,
  Diagnosis: Seq[BasicCode] = Seq.empty,
  ReasonForVisit: Seq[BasicCode] = Seq.empty
) extends DateStamped
