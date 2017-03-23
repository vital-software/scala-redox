package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.EmptyResponse
import com.github.vitalsoftware.scalaredox.models.ResultsMessage
import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions

/**
  * Created by apatzer on 3/23/17.
  */
class ResultsTest extends Specification with NoTimeConversions with RedoxTest {

  "alter Results" should {

    "post new Results" in {
      val json =
        """
          |{
          |	"Meta": {
          |		"DataModel": "Results",
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
          |		"Notes": [],
          |		"Contacts": [
          |			{
          |				"FirstName": "Barbara",
          |				"MiddleName": null,
          |				"LastName": "Bixby",
          |				"Address": {
          |					"StreetAddress": "4762 Hickory Street",
          |					"City": "Monroe",
          |					"State": "WI",
          |					"ZIP": "53566",
          |					"County": "Green",
          |					"Country": "US"
          |				},
          |				"PhoneNumber": {
          |					"Home": "+18088675303",
          |					"Office": "+17077543758",
          |					"Mobile": "+19189368865"
          |				},
          |				"RelationToPatient": "Mother",
          |				"EmailAddresses": [
          |					"barb.bixby@test.net"
          |				],
          |				"Roles": [
          |					"Emergency Contact"
          |				]
          |			}
          |		]
          |	},
          |	"Orders": [
          |		{
          |			"ID": "157968300",
          |			"ApplicationOrderID": null,
          |			"TransactionDateTime": "2015-05-06T06:00:58.872Z",
          |			"CollectionDateTime": "2015-05-06T06:00:58.872Z",
          |			"CompletionDateTime": "2015-05-06T06:00:58.872Z",
          |			"Notes": [],
          |			"ResultsStatus": "Final",
          |			"Procedure": {
          |				"Code": "49086-2",
          |				"Codeset": null,
          |				"Description": "First trimester maternal screen with nuchal translucency panel"
          |			},
          |			"Provider": {
          |				"NPI": "4356789876",
          |				"FirstName": "Pat",
          |				"LastName": "Granite",
          |				"Credentials": [],
          |				"Address": {
          |					"StreetAddress": null,
          |					"City": null,
          |					"State": null,
          |					"ZIP": null,
          |					"County": null,
          |					"Country": null
          |				},
          |				"Location": {
          |					"Type": null,
          |					"Facility": null,
          |					"Department": null
          |				},
          |				"PhoneNumber": {
          |					"Office": null
          |				}
          |			},
          |			"Status": "Resulted",
          |			"ResponseFlag": "Associated Segments",
          |			"Priority": "Stat",
          |			"Results": [
          |				{
          |					"Code": "TEST0001",
          |					"Codeset": null,
          |					"Description": "Cystic Fibrosis",
          |					"Value": "Positive Result",
          |					"ValueType": "String",
          |					"FileType": null,
          |					"Units": null,
          |					"Notes": [
          |						"This individual is a carrier for Cystic Fibrosis."
          |					],
          |					"AbnormalFlag": "Very Abnormal",
          |					"Status": "Final",
          |					"Producer": {
          |						"ID": "RL001",
          |						"Name": "Redox Lab WI",
          |						"IDType": null,
          |						"Address": {
          |							"StreetAddress": "111 W. Fairchild",
          |							"City": "Madison",
          |							"State": "WI",
          |							"ZIP": "53703",
          |							"County": "Dane",
          |							"Country": "USA"
          |						}
          |					},
          |					"Performer": {
          |						"ID": null,
          |						"IDType": null,
          |						"FirstName": "Bob",
          |						"LastName": "Smith",
          |						"Credentials": [
          |							"MD"
          |						],
          |						"Address": {
          |							"StreetAddress": null,
          |							"City": null,
          |							"State": null,
          |							"ZIP": null,
          |							"County": null,
          |							"Country": null
          |						},
          |						"Location": {
          |							"Type": null,
          |							"Facility": null,
          |							"Department": null
          |						},
          |						"PhoneNumber": "+16085551234"
          |					},
          |					"ReferenceRange": {
          |						"Low": null,
          |						"High": null,
          |						"Text": null
          |					},
          |					"ObservationMethod": {
          |						"Code": null,
          |						"Codeset": null,
          |						"Description": null
          |					}
          |				},
          |				{
          |					"Code": "TEST0004",
          |					"Codeset": null,
          |					"Description": "Primary Carnitine Deficiency",
          |					"Value": "Negative Result",
          |					"ValueType": "String",
          |					"FileType": null,
          |					"Units": null,
          |					"Notes": [],
          |					"AbnormalFlag": "Normal",
          |					"Status": "Final",
          |					"Producer": {
          |						"ID": "RL001",
          |						"Name": "Redox Lab WI",
          |						"IDType": null,
          |						"Address": {
          |							"StreetAddress": "111 W. Fairchild",
          |							"City": "Madison",
          |							"State": "WI",
          |							"ZIP": "53703",
          |							"County": "Dane",
          |							"Country": "USA"
          |						}
          |					},
          |					"Performer": {
          |						"ID": null,
          |						"IDType": null,
          |						"FirstName": "Bob",
          |						"LastName": "Smith",
          |						"Credentials": [
          |							"MD"
          |						],
          |						"Address": {
          |							"StreetAddress": null,
          |							"City": null,
          |							"State": null,
          |							"ZIP": null,
          |							"County": null,
          |							"Country": null
          |						},
          |						"Location": {
          |							"Type": null,
          |							"Facility": null,
          |							"Department": null
          |						},
          |						"PhoneNumber": "+16085551234"
          |					},
          |					"ReferenceRange": {
          |						"Low": null,
          |						"High": null,
          |						"Text": null
          |					},
          |					"ObservationMethod": {
          |						"Code": null,
          |						"Codeset": null,
          |						"Description": null
          |					}
          |				}
          |			]
          |		}
          |	]
          |}
        """.stripMargin

      val data = validateJsonInput[ResultsMessage](json)
      data.Orders must not be empty
      data.Orders.head.Provider must beSome
      data.Orders.head.Results must not be empty
      data.Visit must beNone

      val fut = client.post[ResultsMessage, EmptyResponse](data)
      val maybe = handleResponse(fut)
      maybe must beSome
    }
  }

}
