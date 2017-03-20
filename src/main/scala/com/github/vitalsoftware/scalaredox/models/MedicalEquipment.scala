package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.kifi.macros._

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * This section lists any medical equipment that the patient uses or has been prescribed.
  * @param MedicalEquipmentText Free text form of the medical equipment summary
  * @param MedicalEquipment A list of medical equipment that the patient uses (cane, pacemakers, etc.)
  */
@json case class MedicalEquipmentMessage(
  MedicalEquipmentText: Option[String] = None,
  MedicalEquipment: Seq[MedicalEquipment] = Seq.empty
)

/**
  * Piece of medical equipment.
  *
  * @param Status The current status of the equipment (active, completed, etc.)
  * @param StartDate When the equipment was first put into use. ISO 8601 Format
  * @param Quantity The number of products used
  * @param Product A code representing the actual product. SNOMED CT
  */
@json case class MedicalEquipment(
  Status: String,
  StartDate: Option[DateTime] = None,
  Quantity: Option[String] = None, // Todo Int?
  Product: BasicCode
)
