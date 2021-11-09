package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros.{ json, jsonDefaults }
import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.util.RobustPrimitives

@json case class ExtensionCodeset( code: Option[String] = None, display: Option[String])

@json case class ExtensionFacilityAddress(line: String, city: String, state: String, postalCode: String)

@json case class DeviceIdExtension(url: String, string: String)

@json case class OrderingFacilityNameExtension(url: String, string: String)

@json case class OrderingFacilityAddressExtension(url: String, address: ExtensionFacilityAddress)

@json case class OrderedDateTimeExtension(url: String, dateTime: DateTime)

@json case class OrganizationIdExtension(url: String, string: String)

@json case class DocumentExtension(url: String, string: String)

// Carequality extensions present in Meta object for queries
@json case class SenderOrganizationIdExtension(url: String, string: String) // Carequality Organization OID
@json case class UserIdExtension(url: String, string: String) // Querying user ID from your system
@json case class UserRoleExtension(url: String, coding: ExtensionCodeset ) // SNOMED CT code https://www.hl7.org/fhir/valueset-practitioner-role.html
@json case class PurposeOfUseExtension(url: String, coding: ExtensionCodeset) // Always use { code: "TREATMENT", display: "Treatment" }

@json case class Extension(
  `device-id`: Option[DeviceIdExtension] = None,
  `ordering-facility-name`: Option[OrderingFacilityNameExtension] = None,
  `ordering-facility-address`: Option[OrderingFacilityAddressExtension] = None,
  `ordered-date-time`: Option[OrderedDateTimeExtension] = None,
  `organization-id`: Option[OrganizationIdExtension] = None,
  `sender-organization-id`: Option[SenderOrganizationIdExtension] = None,
  `user-id`: Option[UserIdExtension] = None,
  `user-role`: Option[UserRoleExtension] = None,
  `purpose-of-use`: Option[PurposeOfUseExtension] = None,
)

object Extension extends RobustPrimitives
