package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

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
  def Code: String
  def CodeSystem: Option[String]
  def CodeSystemName: Option[String]
  def Name: Option[String]
}

case class BasicCode(
  Code: String,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None
) extends Code

case class CodeWithText(
  Code: String,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Text: Option[String] = None
)

case class CodeWithStatus(
  Code: String,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  DateTime: Option[DateTime] = None
) extends Code with Status with DateStamped

/**
  * Location of provider or care given.
  *
  * @see https://phinvads.cdc.gov/vads/ViewCodeSystem.action?id=2.16.840.1.113883.6.259
  * Note: Seems duplicative of CareLocation, but described using the generic 'Code' object
  */
case class Location(Address: Address, Type: Code)

/**
  * Location of provider or care given.
  *
  * @param Type Type of location. Examples: Clinic, Department, Home, Nursing Unit, Provider's Office, Phone
  * @param Facility Facility. Example: Community Hospital
  * @param Department Department
  */
case class CareLocation(
  Type: Option[String] = None,
  Facility: Option[String] = None,
  Department: Option[String] = None
)

case class Address(
  StreetAddress: Option[String] = None,
  City: Option[String] = None,
  State: Option[String] = None,
  ZIP: Option[String] = None,
  County: Option[String] = None,
  Country: Option[String] = None
)

case class EmailAddress(Address: String) // Todo email validator?

// In E. 164 Format. (e.g. +16085551234)
case class PhoneNumber(
  Home: Option[String] = None,
  Mobile: Option[String] = None,
  Office: Option[String] = None
)