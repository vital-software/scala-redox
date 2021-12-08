package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.{ BasicCode, Dose, ImmunizationProduct }
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Immunization given.
 *
 * @param DateTime When the immunization was given. ISO 8601 Format
 * @param Route The way in which the immunization was delivered (Intramuscular, Oral, etc.). [Medication Route FDA Value Set](http://www.fda.gov/ForIndustry/DataStandards/StructuredProductLabeling%20/ucm162034.htm)
 * @param Product The vaccination that was given.
 * @param Dose Dosage
 */
@jsonDefaults case class Immunization(
  DateTime: Option[String] = None,
  Route: Option[BasicCode] = None,
  Product: Option[ImmunizationProduct] = None,
  Dose: Option[Dose] = None
)

object Immunization extends RobustPrimitives
