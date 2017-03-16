package com.github.vitalsoftware.scalaredox.models

import java.util.UUID

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/17/17.
  */

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
case class Meta(
  DataModel: String,
  EventType: String, // TODO Enum
  EventDateTime: Option[DateTime] = None,
  Test: Option[Boolean] = None,
  Source: Option[SourceDestination] = None,
  Destinations: Seq[SourceDestination] = Seq.empty,
  Message: Option[NumericIdentifier] = None,
  Transmission: Option[NumericIdentifier] = None
)

// Message source or destination
case class SourceDestination(ID: UUID, Name: String)

// Numeric identifier
case class NumericIdentifier(ID: Long)