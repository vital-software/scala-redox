package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.EmptyResponse
import com.github.vitalsoftware.scalaredox.models.{
  AbnormalFlagTypes,
  OrderResultsStatusTypes,
  Result,
  ResultStatusTypes,
  ResultsMessage,
  ResultsStatusTypes
}
import org.specs2.mutable.Specification

/**
 * Created by apatzer on 3/23/17.
 */
class ResultsTest extends Specification with RedoxTest {
  "alter Results" should {
    "post new Results" in {
      val json =
        """
          |{
          |   "Meta": {
          |      "DataModel": "Results",
          |      "EventType": "New",
          |      "EventDateTime": "2017-10-10T15:07:21.362Z",
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
          |         "Race": "Asian",
          |         "IsHispanic": null,
          |         "MaritalStatus": "Single",
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
          |               {
          |                 "Address": "barb.bixby@test.net"
          |               }
          |            ],
          |            "Roles": [
          |               "Emergency Contact"
          |            ]
          |         }
          |      ]
          |   },
          |   "Orders": [
          |      {
          |         "Extensions": {
          |            "ordering-facility-name": {
          |               "url": "https://api.redoxengine.com/extensions/ordering-facility-name",
          |               "string": "Test Facility"
          |            },
          |            "ordering-facility-address": {
          |               "url": "https://api.redoxengine.com/extensions/ordering-facility-address",
          |               "address": {
          |                  "line": "116 Auburn Drive",
          |                  "city": "El Paso",
          |                  "state": "TX",
          |                  "postalCode": "79936"
          |               }
          |            },
          |            "ordered-date-time": {
          |               "url": "https://api.redoxengine.com/extensions/ordered-date-time",
          |               "dateTime": "2020-04-13T10:33:12.000Z"
          |            }
          |         },
          |         "ID": "157968300",
          |         "ApplicationOrderID": null,
          |         "TransactionDateTime": "2015-05-06T06:00:58.872Z",
          |         "CollectionDateTime": "2015-05-06T06:00:58.872Z",
          |         "CompletionDateTime": "2015-05-06T06:00:58.872Z",
          |         "Notes": [],
          |         "ResultsStatus": "Final",
          |         "Procedure": {
          |            "Code": "49086-2",
          |            "Codeset": null,
          |            "Description": "First trimester maternal screen with nuchal translucency panel"
          |         },
          |         "Provider": {
          |            "NPI": "4356789876",
          |            "FirstName": "Pat",
          |            "LastName": "Granite",
          |            "Credentials": [],
          |            "Address": {
          |               "StreetAddress": null,
          |               "City": null,
          |               "State": null,
          |               "ZIP": null,
          |               "County": null,
          |               "Country": null
          |            },
          |            "Location": {
          |               "Type": null,
          |               "Facility": null,
          |               "Department": null,
          |               "Room": null
          |            },
          |            "PhoneNumber": {
          |               "Office": null
          |            }
          |         },
          |         "Status": "Resulted",
          |         "ResponseFlag": "Associated Segments",
          |         "Priority": "Stat",
          |         "Results": [
          |            {
          |               "Extensions": {
          |                  "device-id": {
          |                     "url": "https://api.redoxengine.com/extensions/device-id",
          |                     "string": "ModelName/DeviceID_Manufacturer_TypeAbbreviation"
          |                  }
          |               },
          |               "Code": "TEST0001",
          |               "Codeset": null,
          |               "Description": "Cystic Fibrosis",
          |               "Specimen": {
          |                  "Source": null,
          |                  "BodySite": null,
          |                  "ID": null
          |               },
          |               "Value": "Positive Result",
          |               "ValueType": "String",
          |               "FileType": null,
          |               "Units": null,
          |               "Notes": [
          |                  "This individual is a carrier for Cystic Fibrosis."
          |               ],
          |               "AbnormalFlag": "Very Abnormal",
          |               "Status": "Final",
          |               "Producer": {
          |                  "ID": "RL001",
          |                  "Name": "Redox Lab WI",
          |                  "IDType": null,
          |                  "Address": {
          |                     "StreetAddress": "111 W. Fairchild",
          |                     "City": "Madison",
          |                     "State": "WI",
          |                     "ZIP": "53703",
          |                     "County": "Dane",
          |                     "Country": "USA"
          |                  }
          |               },
          |               "Performer": {
          |                  "ID": null,
          |                  "IDType": null,
          |                  "FirstName": "Bob",
          |                  "LastName": "Smith",
          |                  "Credentials": [
          |                     "MD"
          |                  ],
          |                  "Address": {
          |                     "StreetAddress": null,
          |                     "City": null,
          |                     "State": null,
          |                     "ZIP": null,
          |                     "County": null,
          |                     "Country": null
          |                  },
          |                  "Location": {
          |                     "Type": null,
          |                     "Facility": null,
          |                     "Department": null,
          |                     "Room": null
          |                  },
          |                  "PhoneNumber": "+16085551234"
          |               },
          |               "ReferenceRange": {
          |                  "Low": null,
          |                  "High": null,
          |                  "Text": null
          |               },
          |               "ObservationMethod": {
          |                  "Code": null,
          |                  "Codeset": null,
          |                  "Description": null
          |               }
          |            },
          |            {
          |               "Code": "TEST0004",
          |               "Codeset": null,
          |               "Description": "Primary Carnitine Deficiency",
          |               "Specimen": {
          |                  "Source": null,
          |                  "BodySite": null,
          |                  "ID": null
          |               },
          |               "Value": "Negative Result",
          |               "ValueType": "String",
          |               "FileType": null,
          |               "Units": null,
          |               "Notes": [],
          |               "AbnormalFlag": "Normal",
          |               "Status": "Final",
          |               "Producer": {
          |                  "ID": "RL001",
          |                  "Name": "Redox Lab WI",
          |                  "IDType": null,
          |                  "Address": {
          |                     "StreetAddress": "111 W. Fairchild",
          |                     "City": "Madison",
          |                     "State": "WI",
          |                     "ZIP": "53703",
          |                     "County": "Dane",
          |                     "Country": "USA"
          |                  }
          |               },
          |               "Performer": {
          |                  "ID": null,
          |                  "IDType": null,
          |                  "FirstName": "Bob",
          |                  "LastName": "Smith",
          |                  "Credentials": [
          |                     "MD"
          |                  ],
          |                  "Address": {
          |                     "StreetAddress": null,
          |                     "City": null,
          |                     "State": null,
          |                     "ZIP": null,
          |                     "County": null,
          |                     "Country": null
          |                  },
          |                  "Location": {
          |                     "Type": null,
          |                     "Facility": null,
          |                     "Department": null,
          |                     "Room": null
          |                  },
          |                  "PhoneNumber": "+16085551234"
          |               },
          |               "ReferenceRange": {
          |                  "Low": null,
          |                  "High": null,
          |                  "Text": null
          |               },
          |               "ObservationMethod": {
          |                  "Code": null,
          |                  "Codeset": null,
          |                  "Description": null
          |               }
          |            }
          |         ]
          |      }
          |   ]
          |}
        """.stripMargin

      val data = validateJsonInput[ResultsMessage](json)
      data.Orders must not be empty
      data.Visit must beNone

      val order = data.Orders.head
      order.Provider must beSome
      order.Status must be(OrderResultsStatusTypes.Resulted)
      order.ResultsStatus must beSome(ResultsStatusTypes.Final)

      val provider = order.Provider.get
      provider.NPI must beSome
      order.Procedure must beSome
      order.Results must not be empty

      val result = order.Results.head
      result.Producer must beSome
      result.Extensions must beSome
      result.AbnormalFlag must beSome(AbnormalFlagTypes.VeryAbnormal)
      result.Status must beSome(ResultStatusTypes.Final)

      val extension = result.Extensions.get
      extension.`device-id` must beSome

      val fut = client.post[ResultsMessage, EmptyResponse](data)
      val maybe = handleResponse(fut)
      maybe must beSome
    }
  }
}
