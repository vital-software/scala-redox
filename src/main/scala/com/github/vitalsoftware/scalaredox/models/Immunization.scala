package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Immunization product (i.e. vaccine)
 *
 * @see [CVX code system](http://phinvads.cdc.gov/vads/ViewCodeSystem.action?id=2.16.840.1.113883.12.292)
 *
 * @param Code The vaccination that was given.
 * @param Manufacturer Name of organization that manufacture the immunization. Free text
 * @param LotNumber The lot number of the vaccine
 */
@jsonDefaults case class ImmunizationProduct(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Manufacturer: Option[String] = None,
  LotNumber: Option[String] = None
) extends Code

object ImmunizationProduct extends RobustPrimitives

/**
 * @see [UCUM Units of Measure](http://unitsofmeasure.org/ucum.html)
 *
 * @param Quantity The size of the dose
 * @param Units The units of the dose
 */
@jsonDefaults case class Dose(
  Quantity: Option[String] = None,
  Units: Option[String] = None
)

object Dose extends RobustPrimitives

/**
 * Immunization given.
 *
 * @param DateTime When the immunization was given. ISO 8601 Format
 * @param Route The way in which the immunization was delivered (Intramuscular, Oral, etc.). [Medication Route FDA Value Set](http://www.fda.gov/ForIndustry/DataStandards/StructuredProductLabeling%20/ucm162034.htm)
 * @param Product The vaccination that was given.
 * @param Dose Dosage
 */
@jsonDefaults case class Immunization(
  DateTime: DateTime,
  Route: Option[BasicCode] = None,
  Product: ImmunizationProduct,
  Dose: Option[Dose] = None
) extends DateStamped

object Immunization extends RobustPrimitives

/**
 * This section lists the patient's current immunization status and pertinent immunization history.
 */
@jsonDefaults case class ImmunizationsMessage(
  ImmunizationText: Option[String] = None,
  Immunizations: Seq[Immunization] = Seq.empty
)

object ImmunizationsMessage extends RobustPrimitives
