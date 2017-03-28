package com.github.vitalsoftware.scalaredox.models

import javax.measure.Quantity

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
import si.uom.NonSI
import systems.uom.unicode.CLDR
import tec.uom.se.quantity.Quantities
import tec.uom.se.unit.MetricPrefix

/**
  * Created by apatzer on 3/20/17.
  */

/**
  * @param DateTime The date and time of the reading. ISO 8601 Format
  * @param Observations The type of vital sign being read (height, weight, blood pressure, etc.).
  *                     Subset of LOINC codes (HITSP Vital Sign Result Type).
  */
@jsonDefaults case class VitalSigns(
  DateTime: DateTime,
  Observations: Seq[Observation] = Seq.empty
)

object CommonVitalTypes extends Enumeration {
  val Height, Weight, Oximetry, Temperature, RespirationRate, Pulse, BPSystolic, BPDiastolic = Value
}

object VitalSigns {

  // Easy to create Vital Signs
  def create(vitals: Seq[(CommonVitalTypes.Value, Any)]): Unit = {
    val coded: Seq[(BasicCode, Any, Option[String])] = Seq.empty

    // TODO: Use systems.uom.ucum.UCUM when released in late March 2017
    Quantities.getQuantity(0, MetricPrefix.KILO(CLDR.GRAM))
    Quantities.getQuantity(0, MetricPrefix.CENTI(CLDR.METER))
    Quantities.getQuantity(0, NonSI.MILLIMETRE_OF_MERCURY)


    /* vitals.map { v =>
      // Todo: Lookup UCUM unit
      // Todo: Use CodeProvider or hardcode to LOINC
    }*/
    VitalSigns(DateTime.now(), coded.map((createObservation _).tupled))
  }

  // Create a single vital sign observation
  def createObservation(code: BasicCode, value: Any, units: Option[String]): Observation = {
    val vt = value match {
      case java.lang.Number => ValueTypes.Numeric
      case DateTime => ValueTypes.DateTime
      case _ => ValueTypes.String
    }

    Observation(
      Code = code.Code,
      CodeSystem = code.CodeSystem,
      CodeSystemName = code.CodeSystemName,
      Name = code.Name,
      DateTime = DateTime.now(),
      Status = Some("completed"),
      Value = Some(value.toString),
      ValueType = Some(vt),
      Units = units
    )
  }
}

/**
  * This sections contains all vital sign readings for a patient recorded over time.
  *
  * @param VitalSignsText Free text form of the vital signs summary
  * @param VitalSigns An array of groups of vital signs. Each element represents one time period
  *                   in which vitals were recorded.
  */
@jsonDefaults case class VitalSignsMessage(
  VitalSignsText: Option[String] = None,
  VitalSigns: Seq[VitalSigns] = Seq.empty
)
