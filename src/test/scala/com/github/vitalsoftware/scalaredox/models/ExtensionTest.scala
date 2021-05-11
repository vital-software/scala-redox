package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.scalaredox.RedoxTest
import org.specs2.mutable.Specification

class ExtensionTest extends Specification with RedoxTest {
  "Extension" should {
    "work" in {
      val json =
        """{"device-id": {
          | "url": "https://api.redoxengine.com/extensions/device-id",
          | "string": "ModelName/DeviceID_Manufacturer_TypeAbbreviation"
          |}}""".stripMargin
      val data = validateJsonInput[Extension](json)
      data.`device-id` must beSome
    }
    "deserialize with any valid key" in {
      val json =
        """{
          |  "device-id": {
          |    "url": "https://api.redoxengine.com/extensions/device-id",
          |    "string": "ModelName/DeviceID_Manufacturer_TypeAbbreviation"
          |   },
          |   "ordering-facility-name": {
          |      "url": "https://api.redoxengine.com/extensions/ordering-facility-name",
          |      "string": "Test Facility"
          |   },
          |   "ordering-facility-address": {
          |      "url": "https://api.redoxengine.com/extensions/ordering-facility-address",
          |      "address": {
          |         "line": "116 Auburn Drive",
          |         "city": "El Paso",
          |         "state": "TX",
          |         "postalCode": "79936"
          |      }
          |   },
          |   "ordered-date-time": {
          |      "url": "https://api.redoxengine.com/extensions/ordered-date-time",
          |      "dateTime": "2020-04-13T10:33:12.000Z"
          |   }
          |}""".stripMargin
      val data = validateJsonInput[Extension](json)
      data.`device-id` must beSome
    }
    "ignore unknown keys" in {
      val json =
        """{"nuh-uh 🤔": {
          | "url": "https://api.redoxengine.com/extensions/device-id",
          | "string": "ModelName/DeviceID_Manufacturer_TypeAbbreviation"
          |}}""".stripMargin
      val data =  validateJsonInput[Extension](json)
      data must be
    }
  }
}
