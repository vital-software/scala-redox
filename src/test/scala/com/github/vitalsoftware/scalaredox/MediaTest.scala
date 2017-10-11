package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.EmptyResponse
import com.github.vitalsoftware.scalaredox.models.MediaMessage
import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions

/**
 * Created by apatzer on 3/23/17.
 */
class MediaTest extends Specification with NoTimeConversions with RedoxTest {

  "alter Media" should {

    "post new Media" in {
      val json =
        """
          |{
          |	"Meta": {
          |		"DataModel": "Media",
          |		"EventType": "New",
          |		"EventDateTime": "2017-03-14T19:35:06.047Z",
          |		"Test": true,
          |		"Source": {
          |			"ID": "7ce6f387-c33c-417d-8682-81e83628cbd9",
          |			"Name": "Redox Dev Tools"
          |		},
          |		"Destinations": [
          |			{
          |				"ID": "af394f14-b34a-464f-8d24-895f370af4c9",
          |				"Name": "Redox EMR"
          |			}
          |		],
          |		"Message": {
          |			"ID": 5565
          |		},
          |		"Transmission": {
          |			"ID": 12414
          |		},
          |		"FacilityCode": null
          |	},
          |	"Patient": {
          |		"Identifiers": [
          |			{
          |				"ID": "0000000001",
          |				"IDType": "MR"
          |			},
          |			{
          |				"ID": "e167267c-16c9-4fe3-96ae-9cff5703e90a",
          |				"IDType": "EHRID"
          |			},
          |			{
          |				"ID": "a1d4ee8aba494ca^^^&1.3.6.1.4.1.21367.2005.13.20.1000&ISO",
          |				"IDType": "NIST"
          |			}
          |		],
          |		"Demographics": {
          |			"FirstName": "Timothy",
          |			"MiddleName": "Paul",
          |			"LastName": "Bixby",
          |			"DOB": "2008-01-06",
          |			"SSN": "101-01-0001",
          |			"Sex": "Male",
          |			"Race": "Asian",
          |			"IsHispanic": null,
          |			"MaritalStatus": "Single",
          |			"IsDeceased": null,
          |			"DeathDateTime": null,
          |			"PhoneNumber": {
          |				"Home": "+18088675301",
          |				"Office": null,
          |				"Mobile": null
          |			},
          |			"EmailAddresses": [],
          |			"Language": "en",
          |			"Citizenship": [],
          |			"Address": {
          |				"StreetAddress": "4762 Hickory Street",
          |				"City": "Monroe",
          |				"State": "WI",
          |				"ZIP": "53566",
          |				"County": "Green",
          |				"Country": "US"
          |			}
          |		},
          |		"Notes": []
          |	},
          |	"Visit": {
          |		"VisitNumber": "1234"
          |	},
          |	"Media": {
          |		"FileType": "PDF",
          |		"FileName": "SamplePDF",
          |		"FileContents": "<...base 64 file contents...>",
          |		"DocumentType": "Empty File",
          |		"DocumentID": "b169267c-10c9-4fe3-91ae-9ckf5703e90l",
          |		"Provider": {
          |			"ID": "4356789876",
          |			"IDType": "NPI",
          |			"FirstName": "Pat",
          |			"LastName": "Granite",
          |			"Credentials": [
          |				"MD"
          |			],
          |			"Address": {
          |				"StreetAddress": "123 Main St.",
          |				"City": "Madison",
          |				"State": "WI",
          |				"ZIP": "53703",
          |				"County": "Dane",
          |				"Country": "USA"
          |			},
          |			"Location": {
          |				"Type": null,
          |				"Facility": null,
          |				"Department": null
          |			},
          |			"PhoneNumber": {
          |				"Office": "+16085551234"
          |			}
          |		},
          |		"Authenticated": "False",
          |		"Availability": "Unavailable",
          |		"Notifications": [
          |			{
          |				"ID": "2434534567",
          |				"IDType": "NPI",
          |				"FirstName": "Sharon",
          |				"LastName": "Chalk",
          |				"Credentials": [
          |					"MD",
          |					"PhD"
          |				],
          |				"Address": {
          |					"StreetAddress": "312 Maple Dr. Suite 400",
          |					"City": "Verona",
          |					"State": "WI",
          |					"ZIP": "53593",
          |					"County": "Dane",
          |					"Country": "USA"
          |				},
          |				"Location": {
          |					"Type": null,
          |					"Facility": null,
          |					"Department": null
          |				},
          |				"PhoneNumber": {
          |					"Office": "+16085559999"
          |				}
          |			},
          |			{
          |				"ID": "8263749385",
          |				"IDType": "NPI",
          |				"FirstName": "Jim",
          |				"LastName": "Mica",
          |				"Credentials": [
          |					"RN"
          |				],
          |				"Address": {
          |					"StreetAddress": "5235 Kennedy Ave.",
          |					"City": "Creve Cour",
          |					"State": "MO",
          |					"ZIP": "63141",
          |					"County": "Saint Louis",
          |					"Country": "USA"
          |				},
          |				"Location": {
          |					"Type": null,
          |					"Facility": null,
          |					"Department": null
          |				},
          |				"PhoneNumber": {
          |					"Office": "+13145557777"
          |				}
          |			}
          |		]
          |	}
          |}
        """.stripMargin

      val data = validateJsonInput[MediaMessage](json)
      data.Media.Provider must beSome
      data.Media.Notifications must not be empty

      val fut = client.post[MediaMessage, EmptyResponse](data)
      val maybe = handleResponse(fut)
      maybe must beSome
    }
  }
}
