package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros._
//import ai.x.play.json.Jsonx

/**
 * Created by apatzer on 3/20/17.
 */

/**
 * @param Meta Message header
 * @param Patient List of IDs and IDTypes for the patient
 */
@jsonDefaults case class PatientQuery(Meta: Meta, Patient: Patient)

/**
 * A Clinical Summary represents a snapshot of the patient's chart at a moment in time. It is structured in sections,
 * each focusing on a different aspect of the patient's chart, such as allergies, immunizations, and medications.
 * The full list of sections is at the left.
 *
 * You can obtain a Clinical Summary from an EHR via Query. You can send a Clinical Summary to an EHR via Push.
 */
@jsonDefaults case class ClinicalSummary(
  Meta: Meta,
  Header: Header,
  AdvanceDirectives: Seq[AdvanceDirective] = Seq.empty,
  Allergies: Seq[Allergy] = Seq.empty,
  Encounters: Seq[Encounter] = Seq.empty,
  FamilyHistory: Seq[FamilyHistory] = Seq.empty,
  Immunizations: Seq[Immunization] = Seq.empty,
  MedicalEquipment: Seq[MedicalEquipment] = Seq.empty,
  Medications: Seq[MedicationTaken] = Seq.empty,
  PlanOfCare: Option[PlanOfCare] = None,
  Problems: Seq[Problem] = Seq.empty,
  Procedures: Option[Procedures] = None,
  Results: Seq[Result] = Seq.empty,
  SocialHistory: Option[SocialHistory] = None,
  VitalSigns: Seq[VitalSigns] = Seq.empty
) extends ClinicalSummaryLike

/**
 * TODO: Running into Function22 and Tuple22 limits here...
 * https://stackoverflow.com/questions/20258417/how-to-get-around-the-scala-case-class-limit-of-22-fields
 * Case class is fine, but Scala macro for Json formatting is not.
 * Temporary solution is to comment a few items out...
 * Better solution is use play-json-extensions to support OFormat for 22+ field case classes
 * See pull request at https://github.com/xdotai/play-json-extensions/pull/34
 *
 * @param AllergyText Free text form of the allergies summary
 * @param Allergies A code for the type of allergy intolerance this is (food, drug, etc.). [Allergy/Adverse Event Type Value Set](http://phinvads.cdc.gov/vads/ViewValueSet.action?id=7AFDBFB5-A277-DE11-9B52-0015173D1785)
 * @param AssessmentText Free text of the clinician's conclusions and working assumptions that will guide treatment of the patient
 * @param Assessment An array of diagnoses
 * @param ChiefComplaintText Free text chief complaint with any documented exposition
 * @param EncountersText Free text form of the encounters summary
 * @param Encounters A code describing the type of encounter (office visit, hospital, etc). CPT-4
 * @param HistoryOfPresentIllness Free text describing the history related to the reason for the encounter
 * @param InstructionsText Free text patient education and/or instructions
 * @param InterventionsText Free text about interventions given (procedural, education, application of assistive equipment, etc.)
 * @param MedicationsText Free text form of the medications summary
 * @param Medications Array of medications
 * @param ObjectiveText Free text pertaining to data that can be quantified or categorized
 * @param PhysicalExamText Free text regarding the physical exam, observations by clinician
 * @param PlanOfCare Free text form of the plan of care summary
 * @param ProblemsText Free text form of the problems summary
 * @param Problems An array of all of patient relevant problems, current and historical.
 * @param ReasonForVisitText Free text describing the patient's reason for the patient's visit
 * @param ResultText Free text form of the results summary
 * @param Results Array of test results for the patient. This can include laboratory results, imaging results, and procedure Results[].
 * @param ReviewOfSystemsText Free text about symptoms and wellbeing of the patient
 * @param SubjectiveText Free text description of the patient's condition as reported by the patient and documented by the clinician
 * @param VitalSignsText Free text form of the vital signs summary
 * @param VitalSigns An array of groups of vital signs. Each element represents one time period in which vitals were recorded.
 */
@jsonDefaults case class Visit(
  Meta: Meta,
  Header: Header,
  AllergyText: Option[String] = None,
  Allergies: Seq[Allergy] = Seq.empty,
  AssessmentText: Option[String] = None,
  Assessment: Option[Assessment] = None,
  ChiefComplaintText: Option[String] = None,
  //EncountersText: Option[String],
  Encounters: Seq[Encounter] = Seq.empty,
  HistoryOfPresentIllness: Option[String] = None,
  //InstructionsText: Option[String] = None,
  //InterventionsText: Option[String] = None,
  MedicationsText: Option[String] = None,
  Medications: Seq[MedicationTaken] = Seq.empty,
  //ObjectiveText: Option[String] = None,
  PhysicalExamText: Option[String] = None,
  //PlanOfCareText: Option[String] = None,
  PlanOfCare: Option[PlanOfCare] = None,
  ProblemsText: Option[String] = None,
  Problems: Seq[Problem] = Seq.empty,
  ReasonForVisitText: Option[String] = None,
  ResultText: Option[String] = None,
  Results: Seq[Result] = Seq.empty,
  ReviewOfSystemsText: Option[String] = None,
  SubjectiveText: Option[String] = None,
  VitalSignsText: Option[String] = None,
  VitalSigns: Seq[VitalSigns] = Seq.empty
) extends ClinicalSummaryLike

// Common structure between Visit and ClinicalSummary
trait ClinicalSummaryLike extends MetaLike {
  def Allergies: Seq[Allergy]
  def Encounters: Seq[Encounter]
  def Medications: Seq[MedicationTaken]
  def PlanOfCare: Option[PlanOfCare]
  def Problems: Seq[Problem]
  def Results: Seq[Result]
  def VitalSigns: Seq[VitalSigns]
}

/*
object Visit {
  implicit lazy val jsonFormat = Jsonx.formatCaseClass[Visit]
}
*/ 