package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaDateFormat
import com.github.vitalsoftware.macros._
import play.api.libs.json.{Format, Reads, Writes}

/**
  * Created by apatzer on 3/23/17.
  */

/**
  *
  * @param ID Identifier of insurance plan
  * @param IDType ID type of insurance plan
  * @param Name Name of insurance plan
  */
@jsonDefaults case class InsurancePlan(
  ID: String,
  IDType: String,
  Name: String
)

/**
  *
  * @param ID ID of insurance company (payor)
  * @param IDType ID type of insurance company (payor)
  * @param Name Name of insurance company (payor)
  * @param Address Insurance company's address
  * @param PhoneNumber Insurance companys phone number. In E. 164 Format (i.e. +16085551234)
  */
@jsonDefaults case class InsuranceCompany(
  ID: String,
  IDType: String,
  Name: String,
  Address: Address,
  PhoneNumber: String
)

object InsuranceRelationshipTypes extends Enumeration {
  val Self, Spouse, Other = Value
  implicit lazy val jsonFormat: Format[InsuranceRelationshipTypes.Value] = Format(Reads.enumNameReads(InsuranceRelationshipTypes), Writes.enumNameWrites)
}

object InsuranceAgreementTypes extends Enumeration {
  val Standard, Unified, Maternity = Value
  implicit lazy val jsonFormat: Format[InsuranceAgreementTypes.Value] = Format(Reads.enumNameReads(InsuranceAgreementTypes), Writes.enumNameWrites)
}

object InsuranceCoverageTypes extends Enumeration {
  val Patient, Clinic, Insurance, Other = Value
  implicit lazy val jsonFormat: Format[InsuranceCoverageTypes.Value] = Format(Reads.enumNameReads(InsuranceCoverageTypes), Writes.enumNameWrites)
}

/** Individual who has the agreement with the insurance company for the related policy */
@jsonDefaults case class InsuredPerson(
  FirstName: Option[String] = None,
  LastName: Option[String] = None,
  DOB: Option[DateTime] = None,
  Address: Option[Address] = None,
  Relationship: Option[String] = None,
  PhoneNumber: Option[PhoneNumber] = None
)

/** Guarantor's Employer */
@jsonDefaults case class Employer(
  Name: Option[String] = None,
  Address: Option[Address] = None,
  PhoneNumber: Option[String] = None
)

/**
  * Person ultimately responsible for the bill of the appointment
  *
  * @param RelationToPatient Relation to the patient. E.x. self, parent
  * @param Type Type of guarantor. E.g. institution, individual
  * @param Employer Guarantor's employer
  */
@jsonDefaults case class Guarantor(
  FirstName: Option[String] = None,
  LastName: Option[String] = None,
  DOB: Option[DateTime] = None,
  Address: Option[Address] = None,
  RelationToPatient: Option[String] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  Type: Option[String] = None,
  Employer: Option[Employer] = None
)

/**
  * List of insurance coverages for the patient
  * @param Plan Insurance plan
  * @param Company Insurance company
  * @param GroupNumber Insurance policy group number
  * @param GroupName Insurance policy group name
  * @param EffectiveDate Effect date of this insurance policy. In YYYY-MM-DD format
  * @param ExpirationDate Expiration date of this insurance policy. In YYYY-MM-DD format
  * @param PolicyNumber Insurance policy number
  * @param AgreementType Type of insurance agreement. One of the following: "Standard", "Unified", "Maternity"
  * @param CoverageType Type of insurance agreement. One of the following: "Patient", "Clinic", "Insurance", "Other". Indicates who will be receiving the bill for the service.
  * @param Insured Individual who has the agreement with the insurance company for the related policy
  */
@jsonDefaults case class Insurance(
  Plan: InsurancePlan,
  Company: InsuranceCompany,
  GroupNumber: Option[String] = None,
  GroupName: Option[String] = None,
  EffectiveDate: Option[DateTime] = None,
  ExpirationDate: Option[DateTime] = None,
  PolicyNumber: Option[String] = None,
  AgreementType: Option[InsuranceAgreementTypes.Value] = None,
  CoverageType: Option[InsuranceCoverageTypes.Value] = None,
  Insured: Option[InsuredPerson] = None
)
