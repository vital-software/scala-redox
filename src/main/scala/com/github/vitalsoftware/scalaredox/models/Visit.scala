package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._

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

// TODO: Running into Function22 and Tuple22 limits here...
// https://stackoverflow.com/questions/20258417/how-to-get-around-the-scala-case-class-limit-of-22-fields
// Case class is fine, but Scala macro for Json formatting is not
@jsonDefaults case class VisitResponse(
  Meta: Meta,
  Header: Header,
  Allergies: Seq[Allergy] = Seq.empty,
  AssessmentText: Option[String] = None,
  //Assessment: Option[Assessment] = None, // TODO Finish once Redox documentation comes back online
  ChiefComplaintText: String,
  EncountersText: Option[String],
  Encounters: Seq[Encounter] = Seq.empty,
  HistoryOfPresentIllness: Option[String] = None,
  InstructionsText: String,
  InterventionsText: String,
  MedicationsText: Option[String] = None,
  Medications: Seq[MedicationTaken] = Seq.empty,
  ObjectiveText: String,
  PhysicalExamText: String,
  PlanOfCare: Option[String] = None,
  ProblemsText: Option[String] = None,
  Problems: Seq[Problem] = Seq.empty,
  ResultText: Option[String] = None,
  Results: Seq[Result] = Seq.empty // TODO Check
  /*
  ReviewOfSystemsText: String,
  SubjectiveText: String,
  VitalSignsText: Option[String] = None,
  VitalSigns: Seq[VitalSigns] = Seq.empty
  */
)

/**
  * Patient visit. Mostly used by models.Order and models.Result
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
@jsonDefaults case class Visit(
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
