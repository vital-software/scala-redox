package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.util.RobustPrimitives

import scala.collection.Seq

sealed trait RedoxMessage extends MetaLike
sealed trait PatientRedoxMessage extends RedoxMessage with HasPatient
sealed trait VisitRedoxMessage extends PatientRedoxMessage with HasVisit

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
  Header: clinicalsummary.Header,
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
  Results: Seq[ChartResult] = Seq.empty,
  ReviewOfSystemsText: Option[String] = None,
  SubjectiveText: Option[String] = None,
  VitalSignsText: Option[String] = None,
  VitalSigns: Seq[VitalSigns] = Seq.empty
)

object Visit extends RobustPrimitives

/**
 * The Flowsheet data model allows integration of discrete clinical data. Flowsheets data includes patient assessment
 * information on both the inpatient and outpatient side. It also includes vitals and any discrete nursing
 * documentation.
 */
@jsonDefaults case class FlowSheetMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[BasicVisitInfo] = None,
  Observations: Seq[FlowsheetObservation] = Nil
) extends MetaLike
    with HasPatient
    with VisitRedoxMessage

object FlowSheetMessage extends RobustPrimitives

/**
 * @param Visit Only Visit.VisitNumber A VisitNumber is highly recommended so that the document can be associated with a specific visit
 */
@jsonDefaults case class MediaMessage(
  Meta: Meta,
  Patient: Patient,
  Media: Media,
  Visit: Option[VisitInfo] = None
) extends MetaLike
    with HasVisitInfo
    with HasPatient
    with VisitRedoxMessage

object MediaMessage extends RobustPrimitives

/**
 * The Medications data model allows real-time notification of new prescriptions and modifications or cancellations to existing ones.
 */
// Todo: Handle Administrations (we don't get this message yet)
@jsonDefaults case class MedicationsMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[VisitInfo] = None,
  Order: MedicationOrder,
) extends MetaLike
    with HasPatient
    with VisitRedoxMessage
    with HasVisitInfo

object MedicationsMessage extends RobustPrimitives

/**
 * @param Visit Requires only VisitNumber + VisitDateTime
 */
@jsonDefaults case class NoteMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[VisitInfo] = None,
  Note: Note,
  Orders: Seq[NoteOrder] = Seq.empty
) extends MetaLike
    with HasPatient
    with HasVisitInfo
    with VisitRedoxMessage

object NoteMessage extends RobustPrimitives

trait OrdersMessageLike extends VisitRedoxMessage with HasVisitInfo {
  def Meta: Meta
  def Patient: Patient
  def Visit: Option[VisitInfo]
  def Orders: Seq[Order]
}

/**
 * Order messages communicate details of diagnostic tests such as labs, radiology imaging, etc.
 */
@jsonDefaults case class OrderMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[VisitInfo] = None,
  Order: Order
) extends OrdersMessageLike {
  def Orders = Seq(Order)
}

object OrderMessage extends RobustPrimitives

@jsonDefaults case class GroupedOrdersMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[VisitInfo] = None,
  Orders: Seq[Order] = Seq.empty
) extends OrdersMessageLike

object GroupedOrdersMessage extends RobustPrimitives

/**
 * Used for both query (without the 'PotentialMatches') and holding the response to a patient search query.
 *
 * Meta.DataModel: "PatientSearch",
 * Meta.EventType: {Query, Response}
 */
@jsonDefaults case class PatientSearch(
  Meta: Meta,
  Patient: Option[Patient] = None,
  PotentialMatches: Seq[Patient] = Seq.empty
) extends RedoxMessage

object PatientSearch extends RobustPrimitives

/**
 * Meta.DataModel: "PatientAdmin",
 * Meta.EventType: {Arrival, Cancel, Discharge, NewPatient, PatientUpdate, PatientMerge, PreAdmit, Registration, Transfer, VisitMerge, VisitUpdate}
 */
@jsonDefaults case class PatientAdminMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[VisitInfo] = None
) extends VisitRedoxMessage
    with HasVisitInfo

object PatientAdminMessage extends RobustPrimitives

/**
 * Results messages communicate results of diagnostic tests such as labs, radiology imaging, etc.
 */
@jsonDefaults case class ResultsMessage(
  Meta: Meta,
  Patient: Patient,
  Orders: Seq[OrderResult] = Seq.empty,
  Visit: Option[VisitInfo] = None
) extends VisitRedoxMessage
    with HasVisitInfo

object ResultsMessage extends RobustPrimitives
