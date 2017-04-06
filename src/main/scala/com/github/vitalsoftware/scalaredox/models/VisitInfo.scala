package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
//import ai.x.play.json.Jsonx
import play.api.libs.json.Format

/**
  * Created by apatzer on 3/17/17.
  */


/**
  * @param VisitNumber ID for the patient visit/encounter. Either this or a timeframe is required. If both are provided, the visit number will be used.
  * @param StartDateTime Beginning of the timeframe for which to request visit summaries. Either this or a visit number is required. If both are provided, the visit number will be used. ISO 8601 Format
  * @param EndDateTime End of the timeframe for which to request visit summaries. The maximum and default timeframe will extend 5 days from the start date. If both are provided, the visit number will be used. ISO 8601 Format
  */
@jsonDefaults case class VisitQueryParams(
  VisitNumber: Option[String] = None,
  StartDateTime: DateTime,
  EndDateTime: Option[DateTime] = None
)

/**
  * This query finds and returns visit summaries for a given patient at the specified health system within the specified
  * timeframe. Include the fields listed in the VisitQuery section in the body of your request. The response will
  * contain these sections: Header, Allergies, Assessment, ChiefComplaint, Encounters, HistoryOfPresentIllness,
  * Instructions, Interventions, Medications, Objective, PhysicalExam, PlanOfCare, Problems, ReasonForReferral,
  * ReasonForVisit, Results, ReviewOfSystems, Subjective, VitalSigns.
  *
  * You must provide the patient's medical record number (MRN) as part of the query. The ID type that corresponds to
  * the MRN varies per health system. You should work with the health system to determine which ID type to send. If you
  * do not already have a patient's MRN on file, you can obtain it using the PatientSearch data model. For testing
  * purposes, the Redox Health System is connected to the NIST Document Sharing Test Facility, which uses the "NIST" ID
  * type for the MRN. The PatientSearch data model will return a NIST ID that you should use with the VisitQuery.
  */
@jsonDefaults case class VisitQuery(
  Meta: Meta,
  Patient: Patient,
  Visit: VisitQueryParams
)

/**
  * @param Value The diagnosis as free text
  * @param DateTime When the diagnosis was recorded. ISO 8601 Format
  * @param IsNegativeIndicator Absent or false for a normal diagnosis, indicates that the diagnosis is explicitly unlikely when true
  * @param Encodings An array of formal designations for this diagnosis
  */
@jsonDefaults case class Diagnosis(
  Value: String,
  DateTime: DateTime,
  IsNegativeIndicator: Option[Boolean],
  Encodings: Seq[CodeSet] = Seq.empty
)

@jsonDefaults case  class Assessment(
  Diagnoses: Seq[Diagnosis] = Seq.empty
)

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
  * EncountersText Free text form of the encounters summary
  * Encounters A code describing the type of encounter (office visit, hospital, etc). CPT-4
  * @param HistoryOfPresentIllness Free text describing the history related to the reason for the encounter
  * InstructionsText Free text patient education and/or instructions
  * InterventionsText Free text about interventions given (procedural, education, application of assistive equipment, etc.)
  * @param MedicationsText Free text form of the medications summary
  * @param Medications Array of medications
  * ObjectiveText Free text pertaining to data that can be quantified or categorized
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
  ChiefComplaintText: String,
  //EncountersText: Option[String],
  //Encounters: Seq[Encounter] = Seq.empty,
  HistoryOfPresentIllness: Option[String] = None,
  //InstructionsText: String,
  //InterventionsText: String,
  MedicationsText: Option[String] = None,
  Medications: Seq[MedicationTaken] = Seq.empty,
  //ObjectiveText: String,
  PhysicalExamText: String,
  PlanOfCare: Option[String] = None,
  ProblemsText: Option[String] = None,
  Problems: Seq[Problem] = Seq.empty,
  ReasonForVisitText: Option[String] = None,
  ResultText: Option[String] = None,
  Results: Seq[Result] = Seq.empty,
  ReviewOfSystemsText: String,
  SubjectiveText: String,
  VitalSignsText: Option[String] = None,
  VitalSigns: Seq[VitalSigns] = Seq.empty
)

/*
object Visit {
  implicit lazy val jsonFormat = Jsonx.formatCaseClass[Visit]
}
*/

/**
  * Information about the visit associate with models.Order and/or models.Result
  *
  * @param Location Location of the appointment
  * @param Duration Length of visit. In minutes
  * @param PatientClass Patient class is used in many EHRs to determine where to put the patient. Examples: Inpatient, Outpatient, Emergency
  * @param Instructions Appointment instructions
  * @param Balance Patient balance due for this visit. This field depends on whether or not the sending system has billing functionality, and whether they calculate this field.
  * @param AttendingProvider ID of the attending provider. This ID is required for Inpatient Visits
  * @param Guarantor Person ultimately responsible for the bill of the appointment
  * @param Insurances List of insurance coverages for the patient
  */
@jsonDefaults case class VisitInfo(
  VisitNumber: Option[String] = None,
  VisitDateTime: Option[DateTime] = None,
  Duration: Option[Double] = None,
  Reason: Option[String] = None,
  PatientClass: Option[String] = None,
  Location: Option[CareLocation] = None,
  PreviousLocation: Option[CareLocation],
  AttendingProvider: Option[Provider] = None,
  ConsultingProvider: Option[Provider] = None,
  ReferringProvider: Option[Provider] = None,
  Guarantor: Option[Guarantor] = None,
  Insurance: Option[Insurance] = None,
  Insurances: Seq[Insurance] = Seq.empty,
  Instructions: Seq[String] = Seq.empty,
  Balance: Option[Double] = None,

  Type: Option[String] = None,                 // Claims[].Visit
  DateTime: Option[DateTime] = None,
  DischargeDateTime: Option[DateTime] = None,

  StartDateTime: Option[DateTime] = None,      // Header.Document.Visit only
  EndDateTime: Option[DateTime] = None
)
