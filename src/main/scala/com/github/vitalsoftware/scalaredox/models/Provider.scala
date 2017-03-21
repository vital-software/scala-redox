package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros._

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
  * @param Type The type of provider for this referral. One of the following: "Referring Provider", "Referred To Provider", "Other", "Patient PCP"
  * @param Credentials List of credentials for the Provider responsible for the document. e.g. MD, PhD
  * @param Address Provider's address
  */
@json case class Provider(
  ID: String,
  IDType: String,
  FirstName: String,
  LastName: String,
  Type: Option[String],
  Credentials: Seq[String] = Seq.empty,
  Address: Option[Address] = None,
  Location: Option[CareLocation] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  EmailAddresses: Seq[EmailAddress] = Seq.empty,
  Role: Option[BasicCode] = None
) extends Person

trait Person {
  def FirstName: String
  def LastName: String
  def Address: Option[Address]
  def PhoneNumber: Option[PhoneNumber]
  def EmailAddresses: Seq[EmailAddress]
}

@json case class BasicPerson(
  FirstName: String,
  LastName: String,
  Address: Option[Address] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  EmailAddresses: Seq[EmailAddress] = Seq.empty,
  Credentials: Seq[String] = Seq.empty
)
