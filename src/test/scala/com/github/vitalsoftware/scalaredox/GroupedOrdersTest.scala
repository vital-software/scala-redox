package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.models.{ Gender, GroupedOrdersMessage, Order }
import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions

class GroupedOrdersTest extends Specification with NoTimeConversions with RedoxTest {
  "alter GroupedOrders" should {
    "post a new GroupedOrders given Redox Dev Tools" in {
      val json =
        """
          |{
          |  "Meta": {
          |    "DataModel": "Order",
          |    "EventType": "GroupedOrders",
          |    "EventDateTime": "2017-10-10T15:07:21.362Z",
          |    "Test": true,
          |    "Source": {
          |      "ID": "7ce6f387-c33c-417d-8682-81e83628cbd9",
          |      "Name": "Redox Dev Tools"
          |    },
          |    "Destinations": [
          |      {
          |        "ID": "af394f14-b34a-464f-8d24-895f370af4c9",
          |        "Name": "Redox EMR"
          |      }
          |    ],
          |    "Message": {
          |      "ID": 5565
          |    },
          |    "Transmission": {
          |      "ID": 12414
          |    },
          |    "FacilityCode": null
          |  },
          |  "Patient": {
          |    "Identifiers": [
          |      {
          |        "ID": "3281527",
          |        "IDType": "CLH MRN"
          |      },
          |      {
          |        "ID": "9651160",
          |        "IDType": "EMPI"
          |      }
          |    ],
          |    "Demographics": {
          |      "FirstName": "Timothy",
          |      "MiddleName": "Paul",
          |      "LastName": "Bixby",
          |      "DOB": "2008-01-06",
          |      "SSN": "101-01-0001",
          |      "Sex": null,
          |      "Race": "Asian",
          |      "IsHispanic": null,
          |      "MaritalStatus": "Single",
          |      "IsDeceased": null,
          |      "DeathDateTime": null,
          |      "PhoneNumber": {
          |        "Home": "+18088675301",
          |        "Office": null,
          |        "Mobile": null
          |      },
          |      "EmailAddresses": [],
          |      "Language": "en",
          |      "Citizenship": [],
          |      "Address": {
          |        "StreetAddress": "4762 Hickory Street",
          |        "City": "Monroe",
          |        "State": "WI",
          |        "ZIP": "53566",
          |        "County": "Green",
          |        "Country": "US"
          |      }
          |    },
          |    "Notes": []
          |  },
          |  "Visit": {
          |    "VisitNumber": "1234",
          |    "AttendingProvider": {
          |      "ID": "4356789876",
          |      "IDType": "NPI",
          |      "FirstName": "Pat",
          |      "LastName": "Granite",
          |      "Credentials": [
          |        "MD"
          |      ],
          |      "Address": {
          |        "StreetAddress": "123 Main St.",
          |        "City": "Madison",
          |        "State": "WI",
          |        "ZIP": "53703",
          |        "County": "Dane",
          |        "Country": "USA"
          |      },
          |      "Location": {
          |        "Type": null,
          |        "Facility": null,
          |        "Department": null,
          |        "Room": null
          |      },
          |      "PhoneNumber": {
          |        "Office": "+16085551234"
          |      }
          |    },
          |    "ConsultingProvider": {
          |      "ID": "2434534567",
          |      "IDType": "NPI",
          |      "FirstName": "Sharon",
          |      "LastName": "Chalk",
          |      "Credentials": [
          |        "MD",
          |        "PhD"
          |      ],
          |      "Address": {
          |        "StreetAddress": "312 Maple Dr. Suite 400",
          |        "City": "Verona",
          |        "State": "WI",
          |        "ZIP": "53593",
          |        "County": "Dane",
          |        "Country": "USA"
          |      },
          |      "Location": {
          |        "Type": null,
          |        "Facility": null,
          |        "Department": null,
          |        "Room": null
          |      },
          |      "PhoneNumber": {
          |        "Office": "+16085559999"
          |      }
          |    },
          |    "ReferringProvider": {
          |      "ID": "4236464757",
          |      "IDType": "NPI",
          |      "FirstName": "John",
          |      "LastName": "Slate",
          |      "Credentials": [
          |        "DO"
          |      ],
          |      "Address": {
          |        "StreetAddress": "500 First St.",
          |        "City": "Clayton",
          |        "State": "MO",
          |        "ZIP": "63105",
          |        "County": "Saint Louis",
          |        "Country": "USA"
          |      },
          |      "Location": {
          |        "Type": null,
          |        "Facility": null,
          |        "Department": null,
          |        "Room": null
          |      },
          |      "PhoneNumber": {
          |        "Office": "+13145554321"
          |      }
          |    },
          |    "Guarantor": {
          |      "Number": "10001910",
          |      "FirstName": "Kent",
          |      "LastName": "Bixby",
          |      "DOB": null,
          |      "Sex": null,
          |      "Spouse": {
          |        "FirstName": "Barbara",
          |        "LastName": "Bixby"
          |      },
          |      "Address": {
          |        "StreetAddress": "4762 Hickory Street",
          |        "City": "Monroe",
          |        "State": "WI",
          |        "ZIP": "53566",
          |        "County": "Green",
          |        "Country": "USA"
          |      },
          |      "PhoneNumber": {
          |        "Home": null,
          |        "Business": null
          |      },
          |      "Type": null,
          |      "RelationToPatient": "Father",
          |      "Employer": {
          |        "Name": "Accelerator Labs",
          |        "Address": {
          |          "StreetAddress": "1456 Old Sauk Road",
          |          "City": "Madison",
          |          "State": "WI",
          |          "ZIP": "53719",
          |          "County": "Dane",
          |          "Country": "USA"
          |        },
          |        "PhoneNumber": "+18083451121"
          |      }
          |    },
          |    "Insurances": [
          |      {
          |        "Plan": {
          |          "ID": "31572",
          |          "IDType": "Payor ID",
          |          "Name": "HMO Deductable Plan",
          |          "Type": null
          |        },
          |        "MemberNumber": null,
          |        "Company": {
          |          "ID": "60054",
          |          "IDType": null,
          |          "Name": "aetna (60054 0131)",
          |          "Address": {
          |            "StreetAddress": "PO Box 14080",
          |            "City": "Lexington",
          |            "State": "KY",
          |            "ZIP": "40512-4079",
          |            "County": "Fayette",
          |            "Country": "US"
          |          },
          |          "PhoneNumber": "+18089541123"
          |        },
          |        "GroupNumber": "847025-024-0009",
          |        "GroupName": "Accelerator Labs",
          |        "EffectiveDate": "2015-01-01",
          |        "ExpirationDate": "2020-12-31",
          |        "PolicyNumber": "9140860055",
          |        "AgreementType": null,
          |        "CoverageType": null,
          |        "Insured": {
          |          "LastName": null,
          |          "FirstName": null,
          |          "Relationship": null,
          |          "DOB": null,
          |          "Address": {
          |            "StreetAddress": null,
          |            "City": null,
          |            "State": null,
          |            "ZIP": null,
          |            "County": null,
          |            "Country": null
          |          }
          |        }
          |      }
          |    ],
          |    "Location": {
          |      "Type": "Inpatient",
          |      "Facility": "RES General Hospital",
          |      "Department": "3N",
          |      "Room": "136"
          |    }
          |  },
          |  "Orders": [
          |    {
          |      "ID": "157968300",
          |      "Status": "New",
          |      "TransactionDateTime": "2015-05-06T06:00:58.872Z",
          |      "CollectionDateTime": "2015-05-06T06:00:58.872Z",
          |      "Specimen": {
          |        "Source": null,
          |        "BodySite": null,
          |        "ID": null
          |      },
          |      "Procedure": {
          |        "Code": "49086-2",
          |        "Codeset": null,
          |        "Description": "First trimester maternal screen with nuchal translucency panel"
          |      },
          |      "Provider": {
          |        "NPI": "4356789876",
          |        "FirstName": "Pat",
          |        "LastName": "Granite",
          |        "Credentials": [
          |          "MD"
          |        ],
          |        "Address": {
          |          "StreetAddress": "123 Main St.",
          |          "City": "Madison",
          |          "State": "WI",
          |          "ZIP": "53703",
          |          "County": "Dane",
          |          "Country": "USA"
          |        },
          |        "Location": {
          |          "Type": null,
          |          "Facility": null,
          |          "Department": null,
          |          "Room": null
          |        },
          |        "PhoneNumber": {
          |          "Office": "+16085551234"
          |        }
          |      },
          |      "OrderingFacility": {
          |        "Name": null,
          |        "Address": {
          |          "StreetAddress": null,
          |          "City": null,
          |          "State": null,
          |          "ZIP": null,
          |          "County": null,
          |          "Country": null
          |        },
          |        "PhoneNumber": null
          |      },
          |      "Priority": "Stat",
          |      "Comments": null,
          |      "Notes": [],
          |      "Diagnoses": [
          |        {
          |          "Code": "Z31.41",
          |          "Codeset": "ICD-10",
          |          "Name": "Encounter for fertility testing",
          |          "Type": null
          |        }
          |      ],
          |      "ClinicalInfo": [
          |        {
          |          "Code": "QUESTION001",
          |          "Codeset": null,
          |          "Description": "Estimated Due Date",
          |          "Value": "2015-10-05",
          |          "Units": null,
          |          "Abbreviation": null,
          |          "Notes": []
          |        },
          |        {
          |          "Code": "QUESTION002",
          |          "Codeset": null,
          |          "Description": "Ethnicity",
          |          "Value": "White",
          |          "Units": null,
          |          "Abbreviation": "W",
          |          "Notes": []
          |        },
          |        {
          |          "Code": "QUESTION010",
          |          "Codeset": null,
          |          "Description": "Is this a twin pregnancy?",
          |          "Value": "Singleton",
          |          "Units": null,
          |          "Abbreviation": "sng",
          |          "Notes": []
          |        },
          |        {
          |          "Code": "QUESTION011",
          |          "Codeset": null,
          |          "Description": "Dating Method",
          |          "Value": "LMP",
          |          "Units": null,
          |          "Abbreviation": "lmp",
          |          "Notes": []
          |        }
          |      ]
          |    },
          |    {
          |      "ID": "194415773",
          |      "Status": "New",
          |      "TransactionDateTime": "2015-05-05T00:00:00.000Z",
          |      "CollectionDateTime": "2015-05-07T06:00:58.872Z",
          |      "Specimen": {
          |        "Source": null,
          |        "BodySite": null,
          |        "ID": null
          |      },
          |      "Procedure": {
          |        "Code": "24356-8",
          |        "Codeset": null,
          |        "Description": "Urinalysis complete panel in Urine"
          |      },
          |      "Provider": {
          |        "NPI": "4356789876",
          |        "FirstName": "Pat",
          |        "LastName": "Granite",
          |        "Credentials": [
          |          "MD"
          |        ],
          |        "Address": {
          |          "StreetAddress": "123 Main St.",
          |          "City": "Madison",
          |          "State": "WI",
          |          "ZIP": "53703",
          |          "County": "Dane",
          |          "Country": "USA"
          |        },
          |        "Location": {
          |          "Type": null,
          |          "Facility": null,
          |          "Department": null,
          |          "Room": null
          |        },
          |        "PhoneNumber": {
          |          "Office": "+16085551234"
          |        }
          |      },
          |      "OrderingFacility": {
          |        "Name": null,
          |        "Address": {
          |          "StreetAddress": null,
          |          "City": null,
          |          "State": null,
          |          "ZIP": null,
          |          "County": null,
          |          "Country": null
          |        },
          |        "PhoneNumber": null
          |      },
          |      "Priority": "Routine",
          |      "Comments": null,
          |      "Notes": [],
          |      "Diagnoses": [
          |        {
          |          "Code": "R10.84",
          |          "Codeset": "ICD-10",
          |          "Name": "Abdominal pain generalized",
          |          "Type": null
          |        }
          |      ],
          |      "ClinicalInfo": [
          |        {
          |          "Code": null,
          |          "Codeset": null,
          |          "Description": null,
          |          "Value": null,
          |          "Units": null,
          |          "Abbreviation": null,
          |          "Notes": []
          |        }
          |      ]
          |    }
          |  ]
          |}
          |
        """.stripMargin

      val data = validateJsonInput[GroupedOrdersMessage](json)

      // Validate our data marshalling
      data.Orders must have size (2)
      data.Orders must contain { o: Order =>
        o.Procedure must beSome
        o.Provider must beSome
        o.Diagnoses must not be empty
        o.ClinicalInfo must not be empty
      }

      data.Visit must beSome
      val visit = data.Visit.get
      visit.AttendingProvider must beSome
      visit.ConsultingProvider must beSome
      visit.ReferringProvider must beSome
      visit.Guarantor must beSome
      visit.Guarantor.get.Employer must beSome
      visit.Insurances must not be empty
      visit.Location must beSome

      val patient = data.Patient
      patient.Demographics must beSome
      patient.Demographics.get.Sex must beEqualTo(Gender.Unknown)
    }
  }
}
