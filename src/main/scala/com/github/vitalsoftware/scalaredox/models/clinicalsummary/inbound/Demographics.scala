package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models._

import scala.collection.Seq

/**
 * //////////////////////////////////////////////////////////
 *              CLINICAL SUMMARY FIELDS
 * //////////////////////////////////////////////////////////
 */
/**
 * About a clinical summary demographics.
 *
 * @param FirstName Required
 * @param LastName Required
 * @param DOB Partially reliable. Patient's date of birth. In ISO 8601 format
 * @param SSN Patient SSN.
 * @param Sex Required
 * @param Address Patient's addresses.
 * @param PhoneNumber Patient's phone numbers.
 * @param EmailAddresses Patient's email addresses.
 * @param Language Patient's primary spoken language. In ISO 639-1 alpha values (e.g. 'en'). http://www.mathguide.de/info/tools/languagecode.html
 * @param Race List at http://phinvads.cdc.gov/vads/ViewValueSet.action?id=66D34BBC-617F-DD11-B38D-00188B398520
 * @param Ethnicity List at https://phinvads.cdc.gov/vads/ViewValueSet.action?id=35D34BBC-617F-DD11-B38D-00188B398520
 * @param Religion List at https://www.hl7.org/fhir/v3/ReligiousAffiliation/index.html
 * @param MaritalStatus List at http://www.hl7.org/FHIR/v2/0002/index.html
 */
@jsonDefaults case class Demographics(
  FirstName: String,
  MiddleName: Option[String] = None,
  LastName: String,
  DOB: String,
  SSN: Option[String] = None,
  Sex: SexType.Value = SexType.Unknown,
  Address: Option[Address] = None,
  PhoneNumber: Option[PhoneNumber] = None,
  EmailAddresses: Seq[EmailAddress] = Seq.empty,
  Language: Option[Language] = None,
  Race: Option[RaceType.Value] = None,
  Ethnicity: Option[String] = None,
  Religion: Option[String] = None,
  MaritalStatus: Option[String] = None
)


