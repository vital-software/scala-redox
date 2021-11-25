package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.scalaredox.models.{ Address, BasicCode }
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Patient identifier
 *
 * @param ID The actual identifier for the patient.
 * @param IDType An ID type associated with identifier (Medical Record Number, etc.)
 */
@jsonDefaults case class Identifier(
  ID: Option[String] = None,
  IDType: Option[String] = None
)

/**
 * Location of provider or care given.
 *
 * @see https://phinvads.cdc.gov/vads/ViewCodeSystem.action?id=2.16.840.1.113883.6.259
 * Note: Seems duplicative of CareLocation, but described using the generic 'Code' object
 */
@jsonDefaults case class Location(
  Address: Option[Address] = None,
  Type: BasicCode = BasicCode(),
  Name: Option[String] = None
)

object Location extends RobustPrimitives
