package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.EmptyResponse
import com.github.vitalsoftware.scalaredox.models.FlowSheetMessage
import org.specs2.mutable.Specification

class FlowSheetTest extends Specification with RedoxTest {

  "alter Flowsheet" should {
    "post new Flowsheet results" in {
      val json =
        """
          |{
          |   "Meta": {
          |      "DataModel": "Flowsheet",
          |      "EventType": "New",
          |      "EventDateTime": "2018-06-07T18:56:11.574Z",
          |      "Test": true,
          |      "Source": {
          |         "ID": "7ce6f387-c33c-417d-8682-81e83628cbd9",
          |         "Name": "Redox Dev Tools"
          |      },
          |      "Destinations": [
          |         {
          |            "ID": "af394f14-b34a-464f-8d24-895f370af4c9",
          |            "Name": "Redox EMR"
          |         }
          |      ],
          |      "Message": {
          |         "ID": 5565
          |      },
          |      "Transmission": {
          |         "ID": 12414
          |      },
          |      "FacilityCode": null
          |   },
          |   "Patient": {
          |      "Identifiers": [
          |         {
          |            "ID": "0000000001",
          |            "IDType": "MR"
          |         },
          |         {
          |            "ID": "e167267c-16c9-4fe3-96ae-9cff5703e90a",
          |            "IDType": "EHRID"
          |         },
          |         {
          |            "ID": "a1d4ee8aba494ca",
          |            "IDType": "NIST"
          |         }
          |      ],
          |      "Demographics": {
          |         "FirstName": "Timothy",
          |         "MiddleName": "Paul",
          |         "LastName": "Bixby",
          |         "DOB": "2008-01-06",
          |         "SSN": "101-01-0001",
          |         "Sex": "Male",
          |         "Race": "White",
          |         "IsHispanic": null,
          |         "MaritalStatus": "Married",
          |         "IsDeceased": null,
          |         "DeathDateTime": null,
          |         "PhoneNumber": {
          |            "Home": "+18088675301",
          |            "Office": null,
          |            "Mobile": null
          |         },
          |         "EmailAddresses": [],
          |         "Language": "en",
          |         "Citizenship": [],
          |         "Address": {
          |            "StreetAddress": "4762 Hickory Street",
          |            "City": "Monroe",
          |            "State": "WI",
          |            "ZIP": "53566",
          |            "County": "Green",
          |            "Country": "US"
          |         }
          |      },
          |      "Notes": [],
          |      "Contacts": [
          |         {
          |            "FirstName": "Barbara",
          |            "MiddleName": null,
          |            "LastName": "Bixby",
          |            "Address": {
          |               "StreetAddress": "4762 Hickory Street",
          |               "City": "Monroe",
          |               "State": "WI",
          |               "ZIP": "53566",
          |               "County": "Green",
          |               "Country": "US"
          |            },
          |            "PhoneNumber": {
          |               "Home": "+18088675303",
          |               "Office": "+17077543758",
          |               "Mobile": "+19189368865"
          |            },
          |            "RelationToPatient": "Mother",
          |            "EmailAddresses": [
          |               "barb.bixby@test.net"
          |            ],
          |            "Roles": [
          |               "Emergency Contact"
          |            ]
          |         }
          |      ]
          |   },
          |   "Visit": {
          |      "VisitNumber": "1234",
          |      "AccountNumber": null,
          |      "Location": {
          |         "Type": null,
          |         "Facility": null,
          |         "Department": null,
          |         "Room": null,
          |         "Bed": null
          |      }
          |   },
          |   "Observations": [
          |      {
          |         "DateTime": "2015-08-13T21:08:57.581Z",
          |         "Value": "110.00",
          |         "ValueType": "Numeric",
          |         "Units": "mmHg",
          |         "Code": "Systolic",
          |         "Codeset": "RedoxEMR",
          |         "Status": null,
          |         "Notes": [
          |            "Observation note."
          |         ],
          |         "Observer": {
          |            "ID": "12312",
          |            "FirstName": "Jimmy",
          |            "LastName": "JimJam"
          |         },
          |         "ReferenceRange": {
          |            "Low": null,
          |            "High": null,
          |            "Text": null
          |         },
          |         "AbnormalFlag": null
          |      },
          |      {
          |         "DateTime": "2015-08-13T21:08:57.581Z",
          |         "Value": "90.00",
          |         "ValueType": "Numeric",
          |         "Units": "mmHg",
          |         "Code": "Diastolic",
          |         "Codeset": "RedoxEMR",
          |         "Status": null,
          |         "Notes": [],
          |         "Observer": {
          |            "ID": "12312",
          |            "FirstName": "Jimmy",
          |            "LastName": "JimJam"
          |         },
          |         "ReferenceRange": {
          |            "Low": null,
          |            "High": null,
          |            "Text": null
          |         },
          |         "AbnormalFlag": null
          |      },
          |      {
          |         "DateTime": "2015-08-13T21:08:57.581Z",
          |         "Value": "55",
          |         "ValueType": "Numeric",
          |         "Units": "beats/min",
          |         "Code": "Diastolic",
          |         "Codeset": "RedoxEMR",
          |         "Status": null,
          |         "Notes": [],
          |         "Observer": {
          |            "ID": "12312",
          |            "FirstName": "Jimmy",
          |            "LastName": "JimJam"
          |         },
          |         "ReferenceRange": {
          |            "Low": null,
          |            "High": null,
          |            "Text": null
          |         },
          |         "AbnormalFlag": null
          |      }
          |   ]
          |}
        """.stripMargin

      val data = validateJsonInput[FlowSheetMessage](json)
      data.Observations must not be empty
      data.Observations.head.Observer must beSome

      val fut = client.post[FlowSheetMessage, EmptyResponse](data)
      val maybe = handleResponse(fut)
      maybe must beSome
    }
  }
}
