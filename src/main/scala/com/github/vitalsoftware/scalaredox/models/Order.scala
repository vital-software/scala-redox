package com.github.vitalsoftware.scalaredox.models

import java.time.LocalDate

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import org.joda.time.DateTime
import play.api.libs.json.{Format, Reads, Writes}

/**
 * Order messages communicate details of diagnostic tests such as labs, radiology imaging, etc.
 *
 * Created by apatzer on 3/23/17.
 */

/**
 *
 * @param Source Source of the specimen. [Allowed values](http://phinvads.cdc.gov/vads/ViewValueSet.action?id=C9271C18-7B67-DE11-9B52-0015173D1785)
 * @param BodySite Body site from which the specimen was collected. [Allowed values](http://www.hl7.org/FHIR/v2/0163/index.html)
 * @param ID ID of the collected specimen
 */
@jsonDefaults case class Specimen(
  Source: Option[String] = None,
  BodySite: Option[String] = None,
  ID: Option[String] = None
)

object OrderPriorityTypes extends Enumeration {
  val Stat = Value("Stat")
  val ASAP = Value("ASAP")
  val Routine = Value("Routine")
  val Preoperative = Value("Preoperative")
  val TimingCritical = Value("Timing Critical")

  implicit lazy val jsonFormat: Format[OrderPriorityTypes.Value] = Format(Reads.enumNameReads(OrderPriorityTypes), Writes.enumNameWrites)
}

/**
 * List of supplementary clinical information associated with the order. Often these are answers to Ask at Order Entry (AOE) questions.
 *
 * @param Code Code for the information element
 * @param CodeSet Code set used to identify the information element. Codeset will be blank for system-defined codes. LOINC is used for a subset of AOE questions.
 * @param Description Description of the information element. For AOEs, this is typically the text of the AOE question
 * @param Value Value of the information element. For AOEs, this is typically the full answer
 * @param Units Units of the value. If the Value is a time range, this may be "WK"
 * @param Abbreviation Abbreviation of the value of the information element. Typically only present for text answer AOEs
 * @param Notes Notes related to the clinical info
 */
@jsonDefaults case class ClinicalInfo(
  Code: String,
  CodeSet: Option[String] = None,
  Description: Option[String] = None,
  Value: Option[String] = None,
  Units: Option[String] = None,
  Abbreviation: Option[String] = None,
  Notes: Seq[String] = Seq.empty
)

/** The "Producer" is typically the Lab which did the resulting. */
@jsonDefaults case class OrderProducer(
  ID: Option[String] = None,
  IDType: Option[String] = None,
  Name: Option[String] = None,
  Address: Option[Address] = None
)

/**
 * @param NPI A National Provider Identifier or NPI is a unique 10-digit identification number issued to health care providers in the United States
 */
@jsonDefaults case class OrderProvider(
  NPI: String,
  FirstName: Option[String] = None,
  LastName: Option[String] = None,
  Type: Option[String],
  Credentials: Seq[String] = Seq.empty,
  Address: Option[Address] = None,
  Location: Option[CareLocation] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  EmailAddresses: Seq[String] = Seq.empty,
  Role: Option[BasicCode] = None
) extends ProviderLike {
  def ID: Option[String] = Some(NPI)
  def IDType: Option[String] = Some("NPI")
}

/** Facility this order was placed in */
@jsonDefaults case class OrderingFacility(
  Name: Option[String] = None,
  Address: Option[Address] = None,
  PhoneNumber: Option[String] = None
)

/**
 * Order messages communicate details of diagnostic tests such as labs, radiology imaging, etc.
 *
 * @param ID ID of the order assigned by the placing system
 * @param TransactionDateTime DateTime the order was placed
 * @param CollectionDateTime DateTime the specimen was collected
 * @param Specimen Source of the specimen.
 * @param Procedure Procedure that was ordered
 * @param Provider Provider making the order
 * @param OrderingFacility Facility this order was placed in
 * @param Priority Priority of the order. One of the following: "Stat", "ASAP", "Routine", "Preoperative", "Timing Critical".
 * @param Expiration Date when the order becomes invalid. In YYYY-MM-DD format
 * @param Comments Clinically relevant comments regarding the order
 * @param Notes Order-level notes
 * @param Diagnoses List of diagnoses associated with this order
 * @param ClinicalInfo List of supplementary clinical information associated with the order. Often these are answers to Ask at Order Entry (AOE) questions.
 */
@jsonDefaults case class Order(
  ID: String,
  TransactionDateTime: Option[DateTime] = None,
  CollectionDateTime: Option[DateTime] = None,
  Specimen: Option[Specimen] = None,
  Procedure: Option[CodeSet] = None,
  Provider: Option[OrderProvider] = None,
  OrderingFacility: Option[OrderingFacility] = None,
  Priority: Option[OrderPriorityTypes.Value] = None,
  Expiration: Option[LocalDate] = None,
  Comments: Option[String] = None,
  Notes: Seq[String] = Seq.empty,
  Diagnoses: Seq[CodeSet] = Seq.empty,
  ClinicalInfo: Seq[ClinicalInfo] = Seq.empty
)

@jsonDefaults case class OrderMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[VisitInfo] = None,
  Order: Order
) extends MetaLike