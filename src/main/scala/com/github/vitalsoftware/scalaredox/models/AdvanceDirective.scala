package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * Advance directive documents that the healthcare organization has on file for the patient.
  *
  * @param Type The value of the advance directive (such as 'Do not resuscitate'). SNOMED CT
  * @param StartDate Effective start date of the advance directive. ISO 8601 Format
  * @param EndDate Effective end date of the advance directive. ISO 8601 Format
  * @param ExternalReference A link to a location where the document can be accessed.
  * @param VerifiedBy A collection of people who verified the advance directive with the patient
  * @param Custodians People legally responsible for the advance directive document.
  */
@jsonDefaults case class AdvanceDirective(
  Type: BasicCode,
  Code: String, // Todo Question: Seems to duplicate 'Type' field
  CodeSystem: String,
  CodeSystemName: Option[String] = None,
  Name: Option[String],
  StartDate: DateTime,
  EndDate: Option[DateTime],
  ExternalReference: Option[String],
  VerifiedBy: Seq[BasicPerson] = Seq.empty, // Todo missing VerifiedBy.DateTime
  Custodians: Seq[BasicPerson] = Seq.empty
) extends Code with DateRange

@jsonDefaults case class AdvanceDirectiveMessage(
  AdvanceDirectivesText: Option[String] = None,
  advanceDirectives: Seq[AdvanceDirective] = Seq.empty
)
