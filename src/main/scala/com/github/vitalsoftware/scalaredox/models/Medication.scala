package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * This section contains the patient's past, current, and future medications.
  *
  * @param MedicationsText Free text form of the medications summary
  * @param Medications Patient medications: past, current, and future
  */
case class MedicationsMessage(
  MedicationsText: Option[String] = None,
  Medications: Seq[MedicationTaken] = Seq.empty
)

trait Medication extends DateRange{
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
  * @param Prescription Whether the medication is a prescription. For a prescription: true. For a patient reported med, or a med administered by a provider: false
  * @param FreeTextSig Free text instructions for the medication. Typically instructing patient on the proper means and timing for the use of the medication
  */
case class MedicationTaken (
  Prescription: Boolean,
  FreeTextSig: Option[String] = None,
  Dose: Option[Dose] = None,
  Rate: Option[Dose] = None,
  Route: Option[BasicCode] = None,
  StartDate: DateTime,
  EndDate: Option[DateTime] = None,
  Frequency: Option[TimePeriod] = None,
  Product: BasicCode
) extends Medication

/**
  * Time period for medication.
  *
  * @see [UCUM Units of Measure](http://unitsofmeasure.org/ucum.html)
  * @param Period How often the patient should be taking the medication.
  * @param Unit Units for how often the patient should be taking the medication
  */
case class TimePeriod(
  Period: String,
  Unit: Option[String]
)