package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * This section contains future appointments, medications, orders, procedures, and services that a
  * patient may be scheduled for or is waiting to be scheduled for.
  */
case class PlanOfCareMessage(
  PlanOfCareText: Option[String] = None,
  PlanOfCare: PlanOfCare
)

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
case class PlanOfCare(
  Orders: Seq[Plan] = Seq.empty,
  Procedures: Seq[Plan] = Seq.empty,
  Encounters: Seq[Plan] = Seq.empty,
  MedicationAdministration: Seq[MedicationPlan] = Seq.empty,
  Supplies: Seq[Plan] = Seq.empty,
  Services: Seq[Plan] = Seq.empty
)

/**
  * The state of the plan (intent, confirmed, etc). @see [Plan of care status](http://wiki.siframework.org/CDA+-+Plan+of+Care+Activity+Entries)
  */
trait PlanStatus {
  def Status: Option[String]
}

/**
  * Lab test or diagnostic procedure.
  */
case class Plan(
  Code: String,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  DateTime: Option[DateTime] = None
) extends Code with PlanStatus

/**
  * Medication to be given.
  */
case class MedicationPlan(
  Status: Option[String] = None,
  Dose: Option[Dose] = None,
  Rate: Option[Dose] = None,
  Route: Option[BasicCode] = None,
  StartDate: DateTime,
  EndDate: Option[DateTime] = None,
  Frequency: Option[TimePeriod] = None,
  Product: BasicCode
) extends Medication with PlanStatus
