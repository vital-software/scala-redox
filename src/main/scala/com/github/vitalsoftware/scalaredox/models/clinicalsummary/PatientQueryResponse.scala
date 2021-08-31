package com.github.vitalsoftware.scalaredox.models.clinicalsummary

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.{
  AdvanceDirective,
  Allergy,
  ChartResult,
  Encounter,
  FamilyHistory,
  Immunization,
  Insurance,
  MedicalEquipment,
  MedicationTaken,
  Meta,
  PlanOfCare,
  Problem,
  Procedures,
  SocialHistory,
  VitalSigns
}
import com.github.vitalsoftware.util.RobustPrimitives
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._
import scala.collection.Seq

/**
 * PATIENT QUERY RESPONSE MODEL
 *
 * @param Meta Request/response clinical summary header meta-data
 * @param Header Message header
 * @param AdvanceDirectives advance directives
 * @param Allergies A code for the type of allergy intolerance this is (food, drug, etc.). [Allergy/Adverse Event Type Value Set](http://phinvads.cdc.gov/vads/ViewValueSet.action?id=7AFDBFB5-A277-DE11-9B52-0015173D1785)
 * @param Encounters A code describing the type of encounter (office visit, hospital, etc). CPT-4
 * @param FamilyHistory Each element of the FamilyHistory is one person in the patient's family.
 * @param Insurances List of insurance coverages for the patient
 * @param MedicalEquipment A list of medical equipment that the patient uses (cane, pacemakers, etc.)
 * @param Medications Array of medications
 * @param PlanOfCare Free text form of the plan of care summary
 * @param Problems An array of all of patient relevant problems, current and historical.
 * @param Procedures A general grouper for all things that CDA considers procedures, grouped into three kinds.
 *                   -Observations - procedures which result in new information about a patient.
 *                   -Procedures - procedures whose immediate and primary outcome is the alteration of the physical condition of the patient.
 *                   -Services (Sometimes called Acts) - procedures which cannot be classified as an observation or a procedure, such as a dressing change, feeding, or teaching.
 * @param Results Array of test results for the patient. This can include laboratory results, imaging results, and procedure Results[].
 * @param SocialHistory Generic observations about the patient's social history
 * @param VitalSigns An array of groups of vital signs. Each element represents one time period in which vitals were recorded.
 */
@jsonDefaults case class PatientQueryResponse(
  Meta: Meta,
  Header: Header,
  AdvanceDirectives: Seq[AdvanceDirective] = Seq.empty,
  Allergies: Seq[Allergy] = Seq.empty,
  Encounters: Seq[Encounter] = Seq.empty,
  FamilyHistory: Seq[FamilyHistory] = Seq.empty,
  Immunizations: Seq[Immunization] = Seq.empty,
  Insurances: Seq[Insurance] = Seq.empty,
  MedicalEquipment: Seq[MedicalEquipment] = Seq.empty,
  Medications: Seq[MedicationTaken] = Seq.empty,
  PlanOfCare: Option[PlanOfCare] = None,
  Problems: Seq[Problem] = Seq.empty,
  Procedures: Option[Procedures] = None,
  Results: Seq[ChartResult] = Seq.empty,
  SocialHistory: Option[SocialHistory] = None,
  VitalSigns: Seq[VitalSigns] = Seq.empty
) extends ClinicalSummaryLike

object PatientQueryResponse extends RobustPrimitives
