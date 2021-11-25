package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.scalaredox.models.{ BasicCode, Dose, MedicationExtension, TimePeriod }
import com.github.vitalsoftware.util.RobustPrimitives
import org.joda.time.DateTime

/**
 * @param Prescription Whether the medication is a prescription. For a prescription: true. For a patient reported med, or a med administered by a provider: false
 * @param FreeTextSig Free text instructions for the medication. Typically instructing patient on the proper means and timing for the use of the medication
 */
@jsonDefaults case class MedicationTaken(
  Prescription: Option[Boolean] = None,
  FreeTextSig: Option[String] = None,
  Dose: Option[Dose] = None,
  Rate: Option[Dose] = None,
  Route: Option[BasicCode] = None,
  Status: Option[String] = None,
  StartDate: Option[DateTime] = None,
  EndDate: Option[DateTime] = None,
  Frequency: Option[TimePeriod] = None,
  NumberOfRefillsRemaining: Option[Double] = None,
  IsPRN: Option[Boolean] = None,
  Product: BasicCode = BasicCode(),
  Extensions: Option[MedicationExtension] = None,
)

object MedicationTaken extends RobustPrimitives
