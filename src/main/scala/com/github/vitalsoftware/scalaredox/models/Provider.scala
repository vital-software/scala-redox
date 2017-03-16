package com.github.vitalsoftware.scalaredox.models

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * Provider responsible for a Document
  *
  * @param ID ID of the Provider responsible for the document. This ID is required for Inpatient Visits
  * @param IDType ID type of the ID for the Provider responsible for the document
  * @param FirstName First name of the Provider responsible for the document
  * @param LastName Last name of the Provider responsible for the document
  * @param Credentials List of credentials for the Provider responsible for the document. e.g. MD, PhD
  * @param Address Provider's address
  */
case class Provider(
  ID: String,
  IDType: String,
  FirstName: String,
  LastName: String,
  Credentials: Seq[String] = Seq.empty,
  Address: Option[Address] = None,
  Location: Option[CareLocation] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  EmailAddresses: Seq[EmailAddress] = Seq.empty,
  Role: Option[Code] = None
) extends Person

trait Person {
  def FirstName: String
  def LastName: String
  def Address: Option[Address]
  def PhoneNumber: Option[PhoneNumber]
  def EmailAddresses: Seq[EmailAddress]
}
