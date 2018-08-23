package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.util.{ HasDefaultReads, RobustPrimitives }
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import org.joda.time.DateTime
import play.api.libs.json.{ Format, Reads, Writes }

/**
 * Created by apatzer on 3/17/17.
 */

/**
 * Result from laboratories, imaging procedures, and other procedures.
 *
 * @param Code The test performed and resulted. LOINC for Lab - SNOMED CT otherwise
 * @param Status The status of the test (In Progress, Final)
 * @param Observations A list of corresponding observations for the test (result components)
 */
@jsonDefaults case class ChartResult(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  Observations: Seq[Observation] = Seq.empty
) extends Code with Status

object ChartResult extends RobustPrimitives

/**
 * Person who produced the order result.
 */
// This is so similar to 'Provider' but 'PhoneNumber' has the same name and a different object type so will not deserialize.
@jsonDefaults case class ResultPerformer(
  ID: Option[String],
  IDType: Option[String],
  FirstName: Option[String],
  LastName: Option[String],
  Type: Option[String],
  Credentials: Seq[String] = Seq.empty,
  Address: Option[Address] = None,
  Location: Option[CareLocation] = None,
  PhoneNumber: Option[String] = None
) extends ProviderLike

object ResultPerformer extends RobustPrimitives

/**
 * Results messages communicate results of diagnostic tests such as labs, radiology imaging, etc.
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
@jsonDefaults case class Result(
  Code: String,
  Codeset: Option[String] = None,
  Description: Option[String] = None,
  Specimen: Option[Specimen] = None,
  Value: String,
  ValueType: ValueTypes.Value = ValueTypes.defaultValue,
  FileType: Option[String] = None,
  Units: Option[String] = None,
  Notes: Seq[String] = Seq.empty,
  AbnormalFlag: Option[String] = None,
  Status: Option[String] = None,
  Producer: Option[OrderProducer] = None,
  Performer: Option[ResultPerformer] = None,
  ReferenceRange: Option[ReferenceRange] = None,
  ObservationMethod: Option[CodeSet] = None
)

object Result extends RobustPrimitives

// Current overall status of the order. One of the following: "Final", "Preliminary", "In Process", "Corrected", "Canceled".
object ResultsStatusTypes extends Enumeration with HasDefaultReads {
  val Final, Preliminary, Corrected, Canceled, Other = Value
  val InProcess = Value("In Process")

  val defaultValue = Other
  implicit lazy val jsonFormat: Format[ResultsStatusTypes.Value] = Format(defaultReads, Writes.enumNameWrites)
}

/**
 * Results from an Order
 *
 * @param ID ID of the order assigned by the placing system
 * @param ApplicationOrderID ID assigned by the application fulfilling the order
 * @param TransactionDateTime DateTime at which the order status was updated.
 * @param CollectionDateTime DateTime the specimen was collected
 * @param CompletionDateTime Date and time the results were composed into a report and released.
 * @param Notes Order-level notes
 * @param ResultsStatus Current overall status of the order. One of the following: "Final", "Preliminary", "In Process", "Corrected", "Canceled".
 * @param Procedure Procedure that was ordered
 * @param Provider Provider making the order
 * @param Status Current status of the order. The default value is "Resulted".
 * @param ResponseFlag Specificity of the response requested from the receiving system. One of the following: "Acknowledgement", "Exceptions", "Replacements", "Associated Segments", "Confirmations" . This list is in increasing specificity, and the value selected will incorpate all previous options. Derived from HL7 Table 0121. The default value is "Associated Segments".
 * @param Priority Priority of the order. One of the following: "Stat", "ASAP", "Routine", "Preoperative", "Timing Critical".
 * @param Results List of result components and their values
 */
@jsonDefaults case class OrderResult(
  ID: String,
  ApplicationOrderID: Option[String] = None,
  TransactionDateTime: Option[DateTime] = None,
  CollectionDateTime: Option[DateTime] = None,
  CompletionDateTime: Option[DateTime] = None,
  Notes: Seq[String] = Seq.empty,
  ResultsStatus: Option[ResultsStatusTypes.Value] = None,
  Procedure: Option[CodeSet] = None,
  Provider: Option[Provider] = None,
  Status: String,
  ResponseFlag: Option[String] = None,
  Priority: Option[OrderPriorityTypes.Value] = None,
  Results: Seq[Result] = Seq.empty
)

object OrderResult extends RobustPrimitives

/**
 * Results messages communicate results of diagnostic tests such as labs, radiology imaging, etc.
 */
@jsonDefaults case class ResultsMessage(
  Meta: Meta,
  Patient: Patient,
  Orders: Seq[OrderResult] = Seq.empty,
  Visit: Option[VisitInfo] = None
) extends MetaLike with HasPatient with HasVisitInfo

object ResultsMessage extends RobustPrimitives
