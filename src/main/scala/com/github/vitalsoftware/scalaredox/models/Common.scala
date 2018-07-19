package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.HasDefaultReads
import play.api.libs.json.{ Format, Reads, Writes }

/**
 * Created by apatzer on 3/17/17.
 */

/**
 * The state of the plan (intent, confirmed, etc). @see [Plan of care status](http://wiki.siframework.org/CDA+-+Plan+of+Care+Activity+Entries)
 */
trait Status {
  def Status: Option[String]
}

trait DateStamped {
  def DateTime: DateTime
}

trait DateRange {
  def StartDate: DateTime
  def EndDate: Option[DateTime]
}

/**
 * Code reference (like a foreign key into a SNOMED, ICD-9/10, or other data set)
 */
trait Code {
  def Code: Option[String]
  def CodeSystem: Option[String]
  def CodeSystemName: Option[String]
  def Name: Option[String]
}

@jsonDefaults case class BasicCode(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None
) extends Code

@jsonDefaults case class CodeWithText(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Text: Option[String] = None
) extends Code

@jsonDefaults case class CodeWithStatus(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  DateTime: Option[DateTime] = None
) extends Code with Status

// Alternative to 'BasicCode' used inconsistently in some data models
@jsonDefaults case class CodeSet(
  Code: Option[String] = None,
  Codeset: Option[String] = None,
  Name: Option[String] = None,
  Type: Option[String] = None,
  Description: Option[String] = None
)

@jsonDefaults case class Address(
  StreetAddress: Option[String] = None,
  City: Option[String] = None,
  State: Option[String] = None,
  ZIP: Option[String] = None,
  County: Option[String] = None,
  Country: Option[String] = None
)

/**
 * Location of provider or care given.
 *
 * @see https://phinvads.cdc.gov/vads/ViewCodeSystem.action?id=2.16.840.1.113883.6.259
 * Note: Seems duplicative of CareLocation, but described using the generic 'Code' object
 */
@jsonDefaults case class Location(
  Address: Address,
  Type: BasicCode,
  Name: Option[String] = None
)

/**
 * Location of provider or care given.
 *
 * @param Type Type of location. Examples: Clinic, Department, Home, Nursing Unit, Provider's Office, Phone
 * @param Facility Facility. Example: Community Hospital
 * @param Department Department
 */
@jsonDefaults case class CareLocation(
  Type: Option[String] = None,
  Facility: Option[String] = None,
  Department: Option[String] = None,
  Room: Option[String] = None,
  Bed: Option[String] = None
)

// In E. 164 Format. (e.g. +16085551234)
@jsonDefaults case class PhoneNumber(
  Home: Option[String] = None,
  Mobile: Option[String] = None,
  Office: Option[String] = None
)

/**
 * Reference range for the result.
 * Numeric result values will use the low and high properties.
 * Non-numeric result values will put the normal value in the text property.
 *
 * @param Low Lower bound for a normal result
 * @param High Upper bound for a normal result
 * @param Text The normal value for non-numeric results
 */
@jsonDefaults case class ReferenceRange(
  Low: Option[Double] = None,
  High: Option[Double] = None,
  Text: Option[String] = None
)

object ValueTypes extends Enumeration with HasDefaultReads {
  val Numeric, String, Date, Time, DateTime = Value
  val CodedEntry = Value("Coded Entry")
  val EncapsulatedData = Value("Encapsulated Data")

  val defaultValue = String
  implicit lazy val jsonFormat: Format[ValueTypes.Value] = Format(defaultReads, Writes.enumNameWrites)
}

/**
 * Coded Observation of a patient.
 *
 * @param TargetSite Where (on or in the body) the observation is made. (e.g. "Entire hand (body structure)"). SNOMED CT
 * @param Interpretation A flag indicating whether or not the observed value is normal, high, or low. [Supported Values](https://www.hl7.org/fhir/v3/ObservationInterpretation/index.html)
 * @param ValueType Data type of the value. One of the following: "Numeric", "String", "Date", "Time", "DateTime", "Coded Entry", "Encapsulated Data". Derived from HL7 Table 0125.
 *                  @param Units The units of the measurement. [UCUM Units of Measure](http://unitsofmeasure.org/ucum.html)
 */
@jsonDefaults case class Observation(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  DateTime: DateTime,
  Status: Option[String] = None,
  Value: Option[String] = None,
  ValueType: Option[ValueTypes.Value] = None,
  Units: Option[String] = None,
  ReferenceRange: Option[ReferenceRange] = None,
  TargetSite: Option[BasicCode] = None, // Used by Procedures
  Interpretation: Option[String] = None, // Used by Result
  Observer: Option[Provider] = None
) extends Code with Status with DateStamped
