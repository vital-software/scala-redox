package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.{ Address, BasicCode, Provider }
import com.github.vitalsoftware.util.RobustPrimitives

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
@jsonDefaults case class Encounter(
  Identifiers: Seq[Identifier] = Seq.empty,
  Type: BasicCode = BasicCode(),
  DateTime: Option[String] = None,
  EndDateTime: Option[String] = None,
  Providers: Seq[Provider] = Seq.empty,
  Locations: Seq[Location] = Seq.empty,
  Diagnosis: Seq[BasicCode] = Seq.empty,
  ReasonForVisit: Seq[BasicCode] = Seq.empty
)

object Encounter extends RobustPrimitives
