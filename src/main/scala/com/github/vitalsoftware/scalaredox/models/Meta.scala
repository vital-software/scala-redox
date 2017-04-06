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
  val ClinicalSummary = Value("Clinical Summary")
  val Claim, Device, Financial, Flowsheet, Inventory, Media, Notes, Order, PatientAdmin, PatientSearch, Referral, Results, Scheduling, SurgicalScheduling, Vaccination = Value

  implicit lazy val jsonFormat: Format[DataModelTypes.Value] = Format(Reads.enumNameReads(DataModelTypes), Writes.enumNameWrites)
}

object RedoxEventTypes extends Enumeration {
  val QueryResponse = Value("Query Response")
  val Query, Response, Push, Submission, Payment, New, Transaction, Update, Deplete, Replace, Delete, Cancel, GroupedOrders,
  Arrival, Discharge, NewPatient, PatientUpdate, PatientMerge, PreAdmit, Registration, Transfer, VisitUpdate,
  Modify, NewUnsolicited, Reschedule, Modification, NoShow, AvailableSlots, AvailableSlotsResponse, Booked, BookedResponse = Value

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