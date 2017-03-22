package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * Medication to be given.
  */
@jsonDefaults case class MedicationPlan(
  Status: Option[String] = None,
  Dose: Option[Dose] = None,
  Rate: Option[Dose] = None,
  Route: Option[BasicCode] = None,
  StartDate: DateTime,
  EndDate: Option[DateTime] = None,
  Frequency: Option[TimePeriod] = None,
  Product: BasicCode
) extends Medication with Status with DateRange

/**
  * @see PlanOfCareMessage
  *
  * @param Orders Future lab tests or other diagnostic procedure.
  * @param Procedures These are procedures that alter the state of the body, such as an appendectomy or hip replacement. SNOMED CT
  * @param Encounters The encounter type that is planned. SNOMED CT
  * @param MedicationAdministration Medications planned.
  * @param Supplies Future supplies that a patient may be given, including implants. SNOMED CT
  * @param Services These are procedures that are service-oriented in nature, such as a dressing change, or feeding a patient. SNOMED CT
  */
@jsonDefaults case class PlanOfCare(
  Orders: Seq[CodeWithStatus] = Seq.empty,
  Procedures: Seq[CodeWithStatus] = Seq.empty,
  Encounters: Seq[CodeWithStatus] = Seq.empty,
  MedicationAdministration: Seq[MedicationPlan] = Seq.empty,
  Supplies: Seq[CodeWithStatus] = Seq.empty,
  Services: Seq[CodeWithStatus] = Seq.empty
)

/**
  * This section contains future appointments, medications, orders, procedures, and services that a
  * patient may be scheduled for or is waiting to be scheduled for.
  */
@jsonDefaults case class PlanOfCareMessage(
  PlanOfCareText: Option[String] = None,
  PlanOfCare: PlanOfCare
)
