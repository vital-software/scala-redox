package com.github.vitalsoftware.scalaredox.models

/**
  * Created by apatzer on 3/17/17.
  */
import java.util.UUID

import org.joda.time.DateTime

/** Request/response header meta-data */
case class Meta(

 // Data model. E.g. Scheduling, Results
 DataModel: String,

 // Type of event. E.g. New, Update
 EventType: String, // TODO Enum

 // DateTime of the event. ISO 8601 Format
 EventDateTime: Option[DateTime] = None,

 // Flag as a test message
 Test: Option[Boolean] = None,

 // Where the message originated. Included in messages from Redox
 Source: Option[SourceDestination] = None,

 // List of destinations to send your message to. All messages must have at least one destination.
 // Queries accept only one destination. Required when sending data to Redox
 Destinations: Seq[SourceDestination] = Seq.empty,

 // Record in Redox that corresponds to the communication sent from the source to Redox. Included in messages from Redox
 Message: Option[NumericIdentifier] = None,

 // Record in Redox that corresponds to the communication sent from Redox to your destination. Included in messages from Redox
 Transmission: Option[NumericIdentifier] = None
)


// Message source or destination
case class SourceDestination(ID: UUID, Name: String)

// Numeric identifier
case class NumericIdentifier(ID: Long)

/** Patient identifier */
case class PatientIdentifier(

  // The actual identifier for the patient.
  ID: String,

  // An ID type associated with identifier (Medical Record Number, etc.)
  IDType: String
)

/** Information about the patient and where the summary came from */
case class Header( // Todo: Optional vs required

  // An object containing metadata about the document being pushed to the destination.
  Document: Document,

  // Patient
  Patient: Patient
)

case class Document(

  // Your application's ID for the document
  ID: String,

  // Provider responsible for this document
  Author: Author,

  Visit: Visit,

  // The language of the document.
  Locale: String, // TODO java.util.Locale

  // The title of the document.
  Title: String,

  // The creation/publishing date/time of the document.
  DateTime: DateTime,

  // The type of document (CCD, progress note, etc.)
  Type: String
)

/** Provider responsible for a Document */
case class Author(

  // ID of the Provider responsible for the document. This ID is required for Inpatient Visits
  ID: String,

  // ID type of the ID for the Provider responsible for the document
  IDType: String,

  // The type of provider for this referral. One of the following: "Referring Provider", "Referred To Provider", "Other", "Patient PCP"
  Type: String, // TODO Enum

  // First name of the Provider responsible for the document
  FirstName: String,

  // Last name of the Provider responsible for the document
  LastName: String,

  // List of credentials for the Provider responsible for the document. e.g. MD, PhD
  Credentials: Seq[String] = Seq.empty,

  // Provider's address
  Address: Option[Address]
)

case class Address(
  StreetAddress: Option[String] = None,
  City: Option[String] = None,
  State: Option[String] = None,
  ZIP: Option[String] = None,
  County: Option[String] = None,
  Country: Option[String] = None
)

case class Location(

  // Type of location. Examples: Clinic, Department, Home, Nursing Unit, Provider's Office, Phone
  Type: Option[String] = None,

  // Facility. Example: Community Hospital
  Facility: Option[String] = None,

  // Department
  Department: Option[String] = None
)

// In E. 164 Format. (e.g. +16085551234)
case class PhoneNumber(
  Home: Option[String] = None,
  Mobile: Option[String] = None,
  Office: Option[String] = None
)

case class Visit( // Todo: Check what's required
  Location: Location,
  StartDateTime: DateTime,
  Reason: String, // The reason for visit.
  EndDateTime: DateTime
)


/** Patient */
case class Patient(
  Identifiers: Seq[PatientIdentifier] = Seq.empty,
  Demographics: Demographics
)

case class Demographics(
  FirstName: String,
  LastName: String,
  DOB: DateTime, // Patient's date of birth. In YYYY-MM-DD format
  SSN: Option[String],
  Sex: String, // Todo: Enum One of the following: Female, Male, Unknown, Other
  Address: Option[Address],
  PhoneNumber: Option[PhoneNumber],
  EmailAddresses: Seq[EmailAddress] = Seq.empty,
  Race: Option[String], // Todo Enum http://phinvads.cdc.gov/vads/ViewValueSet.action?id=66D34BBC-617F-DD11-B38D-00188B398520
  Ethnicity: Option[String], // Todo Enum https://phinvads.cdc.gov/vads/ViewValueSet.action?id=35D34BBC-617F-DD11-B38D-00188B398520
  Religion: Option[String], // Todo Enum https://www.hl7.org/fhir/v3/ReligiousAffiliation/index.html
  MaritalStatus: Option[String] // Todo Enum http://www.hl7.org/FHIR/v2/0002/index.html
)

case class EmailAddress(Address: String) // Todo email validator?

case class AdvanceDirective(
  Type: AdvanceDirectiveType,

  // The value of the advance directive (such as 'Do not resuscitate'). SNOMED CT
  Code: String,

  CodeSystem: String,

  CodeSystemName: Option[String] = None,

  Name: Option[String],

  StartDate: DateTime,
  EndDate: Option[DateTime],

  // A link to a location where the document can be accessed.
  ExternalReference: Option[String],

  VerifiedBy: Seq[Author] = Seq.empty  // todo: Not quite an author...
)

/** The type of advance directive (such as resuscitation). SNOMED CT */
case class AdvanceDirectiveType(
  Code: String,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None
)
