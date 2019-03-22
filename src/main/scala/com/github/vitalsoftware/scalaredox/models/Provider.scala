package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives

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
@jsonDefaults case class Provider(
  ID: Option[String] = None,
  IDType: Option[String] = None,
  FirstName: Option[String] = None,
  LastName: Option[String] = None,
  Type: Option[String] = None,
  Credentials: Seq[String] = Seq.empty,
  Address: Option[Address] = None,
  Location: Option[CareLocation] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  EmailAddresses: Seq[String] = Seq.empty,
  Role: Option[BasicCode] = None
) extends ProviderLike

object Provider extends RobustPrimitives

trait ProviderLike {
  def ID: Option[String]
  def IDType: Option[String]
  def FirstName: Option[String]
  def LastName: Option[String]
  def Type: Option[String]
  def Credentials: Seq[String]
  def Address: Option[Address]
  def Location: Option[CareLocation]
}

trait WithContacts {
  def Address: Option[Address]
  def PhoneNumber: Option[PhoneNumber]
  def EmailAddresses: Seq[String]
}

@jsonDefaults case class BasicPerson(
  FirstName: Option[String] = None,
  LastName: Option[String] = None,
  Address: Option[Address] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  EmailAddresses: Seq[String] = Seq.empty,
  Credentials: Option[String] = None
) extends WithContacts

object BasicPerson extends RobustPrimitives
