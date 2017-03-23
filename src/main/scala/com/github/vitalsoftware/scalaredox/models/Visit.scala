package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * Patient visit
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
  Insurances: Seq[Insurance] = Seq.empty,
  Instructions: Seq[String] = Seq.empty,
  Balance: Option[Double] = None,

  Type: Option[String] = None,                 // Claims[].Visit
  DateTime: Option[DateTime] = None,
  DischargeDateTime: Option[DateTime] = None,

  StartDateTime: Option[DateTime] = None,      // Header.Document.Visit only
  EndDateTime: Option[DateTime] = None
)
