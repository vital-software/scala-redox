package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * Patient
  */
case class Patient(
  Identifiers: Seq[PatientIdentifier] = Seq.empty,
  Demographics: Demographics
)

/**
  * Patient identifier
  *
  * @param ID The actual identifier for the patient.
  * @param IDType An ID type associated with identifier (Medical Record Number, etc.)
  */
case class PatientIdentifier(
  ID: String,
  IDType: String
)

/**
  * About a patient.
  *
  * @param FirstName Required
  * @param LastName Required
  * @param DOB Required. Patient's date of birth. In YYYY-MM-DD format
  * @param Sex Required
  * @param Race List at http://phinvads.cdc.gov/vads/ViewValueSet.action?id=66D34BBC-617F-DD11-B38D-00188B398520
  * @param Ethnicity List at https://phinvads.cdc.gov/vads/ViewValueSet.action?id=35D34BBC-617F-DD11-B38D-00188B398520
  * @param Religion List at https://www.hl7.org/fhir/v3/ReligiousAffiliation/index.html
  * @param MaritalStatus List at http://www.hl7.org/FHIR/v2/0002/index.html
  */
case class Demographics(
  FirstName: String,
  LastName: String,
  DOB: DateTime,
  SSN: Option[String] = None,
  Sex: Sex.Value,
  Address: Option[Address] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  EmailAddresses: Seq[EmailAddress] = Seq.empty,
  Race: Option[String] = None,
  Ethnicity: Option[String] = None,
  Religion: Option[String] = None,
  MaritalStatus: Option[String] = None
) extends Person

object Sex extends Enumeration {
  val Male, Female, Unknown, Other = Value
}

