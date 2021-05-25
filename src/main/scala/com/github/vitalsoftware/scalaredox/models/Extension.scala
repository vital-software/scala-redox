package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros.{json, jsonDefaults}
import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.util.RobustPrimitives

@json case class ExtensionFacilityAddress(line: String, city: String, state: String, postalCode: String)

@json case class DeviceIdExtension(url: String, string: String)

@json case class OrderingFacilityNameExtension(url: String, string: String)

@json case class OrderingFacilityAddressExtension(url: String, address: ExtensionFacilityAddress)

@json case class OrderedDateTimeExtension(url: String, dateTime: DateTime)

@json case class OrganizationIdExtension(url: String, string: String)

@json case class Extension(
  `device-id`: Option[DeviceIdExtension] = None,
  `ordering-facility-name`: Option[OrderingFacilityNameExtension] = None,
  `ordering-facility-address`: Option[OrderingFacilityAddressExtension] = None,
  `ordered-date-time`: Option[OrderedDateTimeExtension] = None,
  `organization-id`: Option[OrganizationIdExtension] = None,
)

object Extension extends RobustPrimitives