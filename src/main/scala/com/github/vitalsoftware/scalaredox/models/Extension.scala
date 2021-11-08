package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros.{ json, jsonDefaults }
import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.util.RobustPrimitives

@jsonDefaults case class CodeSetForExtension(
  Code: Option[String] = None,
  Display: Option[String]
)
object CodesetForExtension extends RobustPrimitives

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
@json case class UserRoleExtension(url: String, coding: CodesetForExtension ) // SNOMED CT code https://www.hl7.org/fhir/valueset-practitioner-role.html
@json case class PurposeOfUseExtension(url: String, string: String) // Always use "treatment"

@json case class Extension(
  `device-id`: Option[DeviceIdExtension] = None,
  `ordering-facility-name`: Option[OrderingFacilityNameExtension] = None,
  `ordering-facility-address`: Option[OrderingFacilityAddressExtension] = None,
  `ordered-date-time`: Option[OrderedDateTimeExtension] = None,
  `organization-id`: Option[OrganizationIdExtension] = None,
  `sender-organization-id`: Option[SenderOrganizationIdExtension] = None,
  `user-id`: Option[UserIdExtension] = None,
  `user-role`: Option[UserRoleExtension] = None,
  `organization-id`: Option[SenderOrganizationIdExtension] = None,
)

object Extension extends RobustPrimitives
