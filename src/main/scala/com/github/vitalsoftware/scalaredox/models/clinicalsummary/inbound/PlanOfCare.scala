package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.{ BasicCode, CodeWithStatus, Dose, TimePeriod }
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Medication to be given.
 */
@jsonDefaults case class MedicationPlan(
  Status: Option[String] = None,
  Dose: Option[Dose] = None,
  Rate: Option[Dose] = None,
  Route: Option[BasicCode] = None,
  StartDate: Option[String] = None,
  EndDate: Option[String] = None,
  Frequency: Option[TimePeriod] = None,
  Product: BasicCode = BasicCode()
)

object MedicationPlan extends RobustPrimitives

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

object PlanOfCare extends RobustPrimitives
