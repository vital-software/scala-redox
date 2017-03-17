package com.github.vitalsoftware.scalaredox.models

/**
  * Created by apatzer on 3/17/17.
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