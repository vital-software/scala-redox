package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Created by apatzer on 3/17/17.
 */

trait Medication extends DateRange {
  /** Dose The size of the dose for pills, capsules, etc. */
  def Dose: Option[Dose]

  /** If the medication is not in dose form, this is the rate at which is administered */
  def Rate: Option[Dose]

  /** The method by which the medication is delivered. @see [Medication Route FDA Value Set](https://phinvads.cdc.gov/vads/ViewValueSet.action?oid=2.16.840.1.113883.3.88.12.3221.8.7) */
  def Route: Option[BasicCode]

  /** When the medication was started. ISO 8601 Format */
  def StartDate: DateTime

  /** When the medication ended. ISO 8601 Format */
  def EndDate: Option[DateTime]

  /** How often the patient should be taking the medication. */
  def Frequency: Option[TimePeriod]

  /** The actual medication given. RxNorm */
  def Product: BasicCode
}

/**
 * Time period for medication.
 *
 * @see [UCUM Units of Measure](http://unitsofmeasure.org/ucum.html)
 * @param Period How often the patient should be taking the medication.
 * @param Unit Units for how often the patient should be taking the medication
 */
@jsonDefaults case class TimePeriod(
  Period: Option[String] = None,
  Unit: Option[String] = None
)

object TimePeriod extends RobustPrimitives

/**
 * @param Prescription Whether the medication is a prescription. For a prescription: true. For a patient reported med, or a med administered by a provider: false
 * @param FreeTextSig Free text instructions for the medication. Typically instructing patient on the proper means and timing for the use of the medication
 */
@jsonDefaults case class MedicationTaken(
  Prescription: Boolean,
  FreeTextSig: Option[String] = None,
  Dose: Option[Dose] = None,
  Rate: Option[Dose] = None,
  Route: Option[BasicCode] = None,
  StartDate: DateTime,
  EndDate: Option[DateTime] = None,
  Frequency: Option[TimePeriod] = None,
  Product: BasicCode = BasicCode()
) extends Medication

object MedicationTaken extends RobustPrimitives

/**
 *
 * @param Code Code for the pharmacy.
 * @param Codeset Code set used to identify the pharmacy.
 * @param Description Description of the pharmacy.
 * @param Address The physical location at which the medication should be dispensed.
 */
@jsonDefaults case class Pharmacy(
  Code: Option[String] = None,
  Codeset: Option[String] = None,
  Description: Option[String] = None,
  Address: Option[Address] = None,
)

object Pharmacy extends RobustPrimitives


/**
 *
 * @param Dose The size of the dose for pills, capsules, etc (UCUM Units of Measure).
 * @param Rate        If the medication is in liquid form, the rate at which it is administered (UCUM Units of Measure).
 * @param Route       Method by which the medication is delivered (Medication Route FDA Value Set).
 * @param Product     A code describing the actual medication given.
 * @param FreeTextSig Free text instructions for the medication.
 *                    Typically instructing patient on the proper means and timing for the use of the medication.
 * @param StartDate   When the medication should be started (ISO 8601 Format).
 * @param EndDate     When the medication should be ended (ISO 8601 Format).
 * @param Frequency   How often the patient should be taking the medication.
 * @param IsPRN       Whether the medication is to be taken on an as-needed basis.
 */
@jsonDefaults case class OrderedMedication(
  Dose: Option[Dose] = None,
  Rate: Option[Dose] = None,
  Route: Option[BasicCode] = None,
  Product: BasicCode,
  FreeTextSig: Option[String] = None,
  StartDate: DateTime,
  EndDate: Option[DateTime] = None,
  Frequency: Option[TimePeriod] = None,
  IsPRN: Option[Boolean] = None,
) extends Medication

object OrderedMedication extends RobustPrimitives


/**
 *
 * @param ID ID assigned by the ordering system
 * @param Notes Order-level notes
 * @param Indications This field identifies the condition or problem for which the drug/treatment was prescribed.
 * @param Priority Priority of the order.
 * @param Pharmacy The pharmacy at which the medication should be dispensed.
 */
@jsonDefaults case class MedicationOrder(
  ID: String,
  Notes: Seq[String] = Seq.empty,
  Medication: OrderedMedication,
  Indications: Seq[BasicCode] = Seq.empty,
  Provider: Option[OrderProvider] = None,
  EnteredBy: Option[Provider] = None,
  VerifiedBy: Option[Provider] = None,
  Priority: Option[String] = None,
  Pharmacy: Option[Pharmacy] = None,
)

object MedicationOrder extends RobustPrimitives


/**
 * The Medications data model allows real-time notification of new prescriptions and modifications or cancellations to existing ones.
 */
// Todo: Handle Administrations (we don't get this message yet)
@jsonDefaults case class MedicationsMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[VisitInfo] = None,
  Order: MedicationOrder,
) extends MetaLike with HasPatient with HasVisitInfo

object MedicationsMessage extends RobustPrimitives
