package com.github.vitalsoftware.scalaredox.models.clinicalsummary

import com.github.vitalsoftware.macros.{json, jsonDefaults}
import com.github.vitalsoftware.scalaredox.models.{AdvanceDirective, Allergy, ChartResult, Encounter, FamilyHistory, Immunization, Insurance, MedicalEquipment, MedicationGiven, MedicationTaken, Meta, PlanOfCare, Problem, Procedures, SocialHistory, VitalSigns}
import com.github.vitalsoftware.util.RobustPrimitives
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites
import play.api.libs.json.{Json, OFormat, Reads, Writes, __}

import scala.collection.Seq

// Have to provide a formatter since VisitPush has more than 22 params -> no unapply method can be generated

/**
 * https://developer.redoxengine.com/data-models/ClinicalSummary.html#VisitPush
 * 
 * @param Meta Request/response clinical summary header meta-data
 * @param Header Message header
 * @param AdvanceDirectives advance directives
 * @param Allergies A code for the type of allergy intolerance this is (food, drug, etc.). [Allergy/Adverse Event Type Value Set](http://phinvads.cdc.gov/vads/ViewValueSet.action?id=7AFDBFB5-A277-DE11-9B52-0015173D1785)
 * @param Encounters A code describing the type of encounter (office visit, hospital, etc). CPT-4
 * @param FamilyHistory Each element of the FamilyHistory is one person in the patient's family.
 * @param Immunizations List of patient immunization codes
 * @param InstructionsText Free text patient education/instructions (plain text only, no markup)
 * @param InterventionsText Free text about interventions given during visit (procedural, education, application of assistive equipment, etc.) (plain text only, no markup)
 * @param Insurances List of insurance coverages for the patient
 * @param MedicalEquipment A list of medical equipment that the patient uses (cane, pacemakers, etc.)
 * @param Medications Array of medications
 * @param MedicationsAdministered Array of medications administered during this visit
 * @param PlanOfCare Free text form of the plan of care summary
 * @param Problems An array of all of patient relevant problems, current and historical.
 * @param Procedures A general grouper for all things that CDA considers procedures, grouped into three kinds.
 *                   -Observations - procedures which result in new information about a patient.
 *                   -Procedures - procedures whose immediate and primary outcome is the alteration of the physical condition of the patient.
 *                   -Services (Sometimes called Acts) - procedures which cannot be classified as an observation or a procedure, such as a dressing change, feeding, or teaching.
 * @param ReasonForReferralText
 * @param ReasonForVisitText
 * @param Results Array of test results for the patient. This can include laboratory results, imaging results, and procedure Results[].
 * @param ReviewOfSystemsText Free text about symptoms and wellbeing of the patient (plain text only, no markup)
 * @param SocialHistory Generic observations about the patient's social history
 * @param SubjectiveText Free text description of the patient's condition as reported by the patient + documented by clinician (plain text only, no markup)
 * @param VitalSigns An array of groups of vital signs. Each element represents one time period in which vitals were recorded.
 */
// Leaving some of these out for now since:
// * JSON serialization relies on the case class' "unapply" function
// * case classes can contain more than 22 constructor args, but tuples are capped at length 22
// * case class with more than 22 constructor args can't generate an "unapply" function
// * VisitPush case class _would_ have 24 constructor args
// * => VisitPush would not be JSON serializable
// Removing things that are included in PatientAdmin^PatientUpdate for now
@jsonDefaults case class VisitPush(
  Meta: Meta,
  Header: Header,
  AdvanceDirectives: Seq[AdvanceDirective] = Seq.empty,
//  Allergies: Seq[Allergy] = Seq.empty,
  Encounters: Seq[Encounter] = Seq.empty,
  FamilyHistory: Seq[FamilyHistory] = Seq.empty,
  Immunizations: Seq[Immunization] = Seq.empty,
  InstructionsText: Option[String] = None,
  InterventionsText: Option[String] = None,
//  Insurances: Seq[Insurance] = Seq.empty,
  MedicalEquipment: Seq[MedicalEquipment] = Seq.empty,
  Medications: Seq[MedicationTaken] = Seq.empty,
  MedicationsAdministered: Seq[MedicationGiven] = Seq.empty,
  ObjectiveText: Option[String] = None,
  PlanOfCare: Option[PlanOfCare] = None,
  Problems: Seq[Problem] = Seq.empty,
  Procedures: Option[Procedures] = None,
  ReasonForReferralText: Option[String] = None,
  ReasonForVisitText: Option[String] = None,
  Results: Seq[ChartResult] = Seq.empty,
  ReviewOfSystemsText: Option[String] = None,
  SocialHistory: Option[SocialHistory] = None,
  SubjectiveText: Option[String] = None,
  VitalSigns: Seq[VitalSigns] = Seq.empty
) extends ClinicalSummaryLike

object VisitPush extends RobustPrimitives
