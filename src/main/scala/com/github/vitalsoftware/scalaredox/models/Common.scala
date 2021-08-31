package com.github.vitalsoftware.scalaredox.models

import java.util.{ Locale, UUID }
import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives
import play.api.libs.json._

import scala.util.Try

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

@jsonDefaults case class Address(
  StreetAddress: Option[String] = None,
  City: Option[String] = None,
  State: Option[String] = None,
  ZIP: Option[String] = None,
  County: Option[String] = None,
  Country: Option[String] = None
)

object Address extends RobustPrimitives

@jsonDefaults case class EmailAddress(
  Address: Option[String] = None,
)

object EmailAddress extends RobustPrimitives

@jsonDefaults case class Log(
  ID: Option[String] = None,
  AttemptID: Option[String] = None,
)

object Log extends RobustPrimitives

@jsonDefaults case class Department(
  Location: Option[String] = None,
)

object Department extends RobustPrimitives

/**
 * Patient identifier
 *
 * @param ID The actual identifier for the patient.
 * @param IDType An ID type associated with identifier (Medical Record Number, etc.)
 */
@jsonDefaults case class Identifier(
  ID: String,
  IDType: String
)

object Identifier extends RobustPrimitives

// Message source or destination
@jsonDefaults case class SourceDestination(ID: UUID, Name: Option[String] = None)

object SourceDestination extends RobustPrimitives

// Numeric identifier
@jsonDefaults case class NumericIdentifier(ID: Long)

object NumericIdentifier extends RobustPrimitives

/**
 * Location of provider or care given.
 *
 * @see https://phinvads.cdc.gov/vads/ViewCodeSystem.action?id=2.16.840.1.113883.6.259
 * Note: Seems duplicative of CareLocation, but described using the generic 'Code' object
 */
@jsonDefaults case class Location(
  Address: Address,
  Type: BasicCode = BasicCode(),
  Name: Option[String] = None
)

object Location extends RobustPrimitives

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

object CareLocation extends RobustPrimitives

// In E. 164 Format. (e.g. +16085551234)
@jsonDefaults case class PhoneNumber(
  Home: Option[String] = None,
  Mobile: Option[String] = None,
  Office: Option[String] = None
)

object PhoneNumber extends RobustPrimitives

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

object ReferenceRange extends RobustPrimitives

object ValueTypes extends Enumeration {
  val Numeric, String, Date, Time, DateTime, PhysicalQuantity, Number, Interval, Ratio, Code = Value
  val CodedEntry = Value("Coded Entry")
  val EncapsulatedData = Value("Encapsulated Data")
  val FormattedText = Value("Formatted Text")

  def defaultValue = String

  @transient implicit lazy val jsonFormat: Format[ValueTypes.Value] =
    Format(Reads.enumNameReads(ValueTypes), Writes.enumNameWrites)
}

/**
 * This wraps java.util.Locale for consistent serialisation form language to an ISO standard language locale. The java
 * implementation doesn't guarantee the validation of the input and the json formats gets masked by play-json's
 * implementation that doesn't handle validation.
 */
case class Language(underlying: Locale) {
  override def toString(): String = underlying.getISO3Language
}

object Language {
  val jsonWrites: Writes[Language] = new Writes[Language] {
    override def writes(o: Language): JsValue = JsString(o.underlying.toString)
  }

  val jsonReads: Reads[Language] = new Reads[Language] {
    override def reads(json: JsValue): JsResult[Language] = json match {
      case JsString("Other") | JsString("Unknown") => JsSuccess(Language(Locale.ROOT))
      case JsString(str) if Try(new Locale(str).getISO3Language).isSuccess =>
        JsSuccess(Language(Locale.forLanguageTag(str)))
      case _ => JsError(Seq(JsPath -> Seq(JsonValidationError("error.expected.locale"))))
    }
  }

  implicit val jsonFormats: Format[Language] = Format(jsonReads, jsonWrites)
}
