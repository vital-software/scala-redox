package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import org.joda.time.DateTime
import play.api.libs.json.{ Format, Reads, Writes }

/**
 * Order messages communicate details of diagnostic tests such as labs, radiology imaging, etc.
 *
 * Created by apatzer on 3/23/17.
 */

/**
 *
 * @param Source Source of the specimen. [Allowed values](http://phinvads.cdc.gov/vads/ViewValueSet.action?id=C9271C18-7B67-DE11-9B52-0015173D1785)
 * @param BodySite Body site from which the specimen was collected. [Allowed values](http://www.hl7.org/FHIR/v2/0163/index.html)
 */
@jsonDefaults case class Specimen(
  Source: Option[String] = None,
  BodySite: Option[String] = None
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

// Current overall status of the order. One of the following: "Final", "Preliminary", "In Process", "Corrected", "Canceled".
object ResultsStatusTypes extends Enumeration {
  val Final, Preliminary, Corrected, Canceled = Value
  val InProcess = Value("In Process")

  implicit lazy val jsonFormat: Format[ResultsStatusTypes.Value] = Format(Reads.enumNameReads(ResultsStatusTypes), Writes.enumNameWrites)
}

/** The "Producer" is typically the Lab which did the resulting. */
@jsonDefaults case class OrderProducer(
  ID: Option[String] = None,
  IDType: Option[String] = None,
  Name: Option[String] = None,
  Address: Address
)

/**
 * @param NPI A National Provider Identifier or NPI is a unique 10-digit identification number issued to health care providers in the United States
 */
@jsonDefaults case class OrderProvider(
  NPI: String,
  FirstName: String,
  LastName: String,
  Type: Option[String],
  Credentials: Seq[String] = Seq.empty,
  Address: Option[Address] = None,
  Location: Option[CareLocation] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  EmailAddresses: Seq[String] = Seq.empty,
  Role: Option[BasicCode] = None
) extends Person

/**
 * Result components and their values
 *
 * @param Value Value of the result component. If ValueType is "Encapsulated Data" this field includes the Redox BLOB URI
 * @param ValueType Data type for the result value. One of the following: "Numeric", "String", "Date", "Time", "DateTime",
 *                  "Coded Entry", "Encapsulated Data". Derived from [HL7 Table 0125](https://phinvads.cdc.gov/vads/ViewValueSet.action?id=86E09BA6-0767-E011-8B0C-00188B39829B).
 * @param FileType If ValueType is "Encapsulated Data", this field includes the type of file. E.g. PDF, JPG
 * @param Units Units of the result
 * @param Notes Notes about the result component/observation
 * @param AbnormalFlag Indication of whether the result was abnormal. One of the following: "Normal", "Low", "Very Low",
 *                     "High", "Very High", "Abnormal", "Very Abnormal". Abnormal flags starting with "Very" indicate a
 *                     panic level. The "High" and "Low" flags should be used with Numeric result values while "Abnormal"
 *                     should be used with non-numeric values.
 * @param Status Current status of the result. One of the following: "Final", "Incomplete", "Preliminary", "Corrected", "Preliminary"
 * @param Producer The "Producer" is typically the Lab which did the resulting.
 * @param Performer The provider who produced this result
 * @param ReferenceRange Reference range for the result. Numeric result values will use the low and high properties.
 *                       Non-numeric result values will put the normal value in the text property.
 * @param ObservationMethod Method used to obtain the observation. This field is used when an observation may be
 *                          obtained by different methods and the sending system wishes to indicate which method was used.
 */
@jsonDefaults case class OrderResult(
  Code: String,
  Codeset: Option[String] = None,
  Description: Option[String] = None,
  Value: String,
  ValueType: ValueTypes.Value,
  FileType: Option[String] = None,
  Units: Option[String] = None,
  Notes: Seq[String] = Seq.empty,
  AbnormalFlag: Option[String] = None,
  Status: Option[String] = None,
  Producer: Option[OrderProducer] = None,
  Performer: Option[Provider] = None,
  ReferenceRange: Option[ReferenceRange] = None,
  ObservationMethod: Option[CodeSet] = None
)

/** Facility this order was placed in */
@jsonDefaults case class OrderingFacility(
  Name: Option[String] = None,
  Address: Option[Address] = None,
  PhoneNumber: Option[String] = None
)

/**
 *
 * @param ID ID of the order assigned by the placing system
 * @param TransactionDateTime DateTime the order was placed
 * @param CollectionDateTime DateTime the specimen was collected
 * @param CompletionDateTime Date and time the results were composed into a report and released.
 * @param Specimen Source of the specimen.
 * @param Procedure Procedure that was ordered
 * @param Provider Provider making the order
 * @param OrderingFacility Facility this order was placed in
 * @param Priority Priority of the order. One of the following: "Stat", "ASAP", "Routine", "Preoperative", "Timing Critical".
 * @param Comments Clinically relevant comments regarding the order
 * @param Notes Order-level notes
 * @param Diagnoses List of diagnoses associated with this order
 * @param ClinicalInfo List of supplementary clinical information associated with the order. Often these are answers to Ask at Order Entry (AOE) questions.
 * @param Status Current status of the order. The default value is "Resulted".
 * @param ResponseFlag Specificity of the response requested from the receiving system. One of the following: "Acknowledgement", "Exceptions", "Replacements", "Associated Segments", "Confirmations" . This list is in increasing specificity, and the value selected will incorpate all previous options. Derived from HL7 Table 0121. The default value is "Associated Segments".
 */
@jsonDefaults case class Order(
  ID: String,
  TransactionDateTime: Option[DateTime] = None,
  CollectionDateTime: Option[DateTime] = None,
  CompletionDateTime: Option[DateTime] = None,
  Specimen: Option[Specimen] = None,
  Procedure: Option[CodeSet] = None,
  Provider: Option[OrderProvider] = None,
  OrderingFacility: Option[OrderingFacility] = None,
  Priority: Option[OrderPriorityTypes.Value] = None,
  Comments: Option[String] = None,
  Notes: Seq[String] = Seq.empty,
  Diagnoses: Seq[CodeSet] = Seq.empty,
  ClinicalInfo: Seq[ClinicalInfo] = Seq.empty,
  ResultsStatus: Option[String] = None,
  Status: Option[String] = None,
  ResponseFlag: Option[String] = None,
  Results: Seq[OrderResult] = Seq.empty
)

@jsonDefaults case class OrderMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[VisitInfo] = None,
  Order: Order
) extends MetaLike