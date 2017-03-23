package com.github.vitalsoftware.scalaredox.models

import java.util.UUID

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
import play.api.libs.json.{Format, Reads, Writes}

/**
  * Created by apatzer on 3/17/17.
  */

object DataModelTypes extends Enumeration {
  val ClinicalSummary = Value("Clinical Summary") // If this one item didn't have a space, this would be much simpler
  val Claim = Value("Claim")
  val Device = Value("Device")
  val Financial = Value("Financial")
  val Flowsheet = Value("Flowsheet")
  val Inventory = Value("Inventory")
  val Media = Value("Media")
  val Notes = Value("Notes")
  val Order = Value("Order")
  val PatientAdmin = Value("PatientAdmin")
  val PatientSearch = Value("PatientSearch")
  val Referral = Value("Referral")
  val Results = Value("Results")
  val Scheduling = Value("Scheduling")
  val SurgicalScheduling = Value("SurgicalScheduling")
  val Vaccination = Value("Vaccination")

  implicit lazy val jsonFormat: Format[DataModelTypes.Value] = Format(Reads.enumNameReads(DataModelTypes), Writes.enumNameWrites)
}

object RedoxEventTypes extends Enumeration {
  val Query = Value("Query")
  val Response = Value("Response")
  val QueryResponse = Value("Query Response") // If this one item didn't have a space, this would be much simpler
  val Push = Value("Push")
  val Submission = Value("Submission")
  val Payment = Value("Payment")
  val New = Value("New")
  val Transaction = Value("Transaction")
  val Update = Value("Update")
  val Deplete = Value("Deplete")
  val Replace = Value("Replace")
  val Delete = Value("Delete")
  val Cancel = Value("Cancel")
  val GroupedOrders = Value("GroupedOrders")
  val Arrival = Value("Arrival")
  val Discharge = Value("Discharge")
  val NewPatient = Value("NewPatient")
  val PatientUpdate = Value("PatientUpdate")
  val PatientMerge = Value("PatientMerge")
  val PreAdmit = Value("PreAdmit")
  val Registration = Value("Registration")
  val Transfer = Value("Transfer")
  val VisitUpdate = Value("VisitUpdate")
  val Modify = Value("Modify")
  val NewUnsolicited = Value("NewUnsolicited")
  val Reschedule = Value("Reschedule")
  val Modification = Value("Modification")
  val NoShow = Value("NoShow")
  val AvailableSlots = Value("AvailableSlots")
  val AvailableSlotsResponse = Value("AvailableSlotsResponse")
  val Booked = Value("Booked")
  val BookedResponse = Value("BookedResponse")

  implicit lazy val jsonFormat: Format[RedoxEventTypes.Value] = Format(Reads.enumNameReads(RedoxEventTypes), Writes.enumNameWrites)
}

// Message source or destination
@jsonDefaults case class SourceDestination(ID: UUID, Name: Option[String] = None)

// Numeric identifier
@jsonDefaults case class NumericIdentifier(ID: Long)

/**
  * Request/response header meta-data
  *
  * @param DataModel Data model. E.g. Scheduling, Results
  * @param EventType Type of event. E.g. New, Update
  * @param EventDateTime DateTime of the event. ISO 8601 Format
  * @param Test Flag as a test message
  * @param Source Where the message originated. Included in messages from Redox
  * @param Destinations List of destinations to send your message to. All messages must have at least one destination. Queries accept only one destination. Required when sending data to Redox
  * @param Message Record in Redox that corresponds to the communication sent from the source to Redox. Included in messages from Redox
  * @param Transmission Record in Redox that corresponds to the communication sent from Redox to your destination. Included in messages from Redox
  */
@jsonDefaults case class Meta(
                               DataModel: DataModelTypes.Value,
                               EventType: RedoxEventTypes.Value,
                               EventDateTime: Option[DateTime] = None,
                               Test: Option[Boolean] = None,
                               Source: Option[SourceDestination] = None,
                               Destinations: Seq[SourceDestination] = Seq.empty,
                               Message: Option[NumericIdentifier] = None,
                               Transmission: Option[NumericIdentifier] = None
)