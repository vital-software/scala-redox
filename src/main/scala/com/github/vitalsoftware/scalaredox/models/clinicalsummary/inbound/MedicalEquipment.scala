package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.BasicCode
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Piece of medical equipment.
 *
 * @param Status The current status of the equipment (active, completed, etc.)
 * @param StartDate When the equipment was first put into use. ISO 8601 Format
 * @param Quantity The number of products used
 * @param Product A code representing the actual product. SNOMED CT
 */
@jsonDefaults case class MedicalEquipment(
  Status: Option[String] = None,
  StartDate: Option[String] = None,
  Quantity: Option[String] = None, // Todo Int?
  Product: BasicCode = BasicCode()
)

object MedicalEquipment extends RobustPrimitives
