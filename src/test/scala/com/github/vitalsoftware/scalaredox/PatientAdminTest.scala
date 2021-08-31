package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.EmptyResponse
import com.github.vitalsoftware.scalaredox.models._
import org.specs2.mutable.Specification

/**
 * Created by apatzer on 3/23/17.
 */
class PatientAdminTest extends Specification with RedoxTest {
  "post PatientAdmin" should {
    "return OK" in {
      val json =
        """
          |{
          |   "Meta": {
          |      "DataModel": "PatientAdmin",
          |      "EventType": "Arrival",
          |      "EventDateTime": "2021-08-20T16:58:51.281Z",
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
          |      "Logs": [
          |         {
          |            "ID": "d9f5d293-7110-461e-a875-3beb089e79f3",
          |            "AttemptID": "925d1617-2fe0-468c-a14c-f8c04b572c54"
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
          |         "MaritalStatus": "Single",
          |         "IsDeceased": null,
          |         "DeathDateTime": null,
          |         "PhoneNumber": {
          |            "Home": "+18088675301",
          |            "Office": null,
          |            "Mobile": null
          |         },
          |         "EmailAddresses": [
          |            "testfake12312@gmail.com"
          |         ],
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
          |      ],
          |      "Diagnoses": [
          |         {
          |            "Code": "R07.0",
          |            "Codeset": "ICD-10",
          |            "Name": "Pain in throat",
          |            "Type": null,
          |            "DocumentedDateTime": null
          |         }
          |      ],
          |      "Allergies": [
          |         {
          |            "Code": "7982",
          |            "Codeset": "RxNorm",
          |            "Name": "Penicillin",
          |            "Type": {
          |               "Code": null,
          |               "Codeset": null,
          |               "Name": null
          |            },
          |            "OnsetDateTime": null,
          |            "Reaction": [
          |               {
          |                  "Code": "28926001",
          |                  "Codeset": "SNOMED CT",
          |                  "Name": "Rash"
          |               },
          |               {
          |                  "Code": "247472004",
          |                  "Codeset": "SNOMED CT",
          |                  "Name": "Hives"
          |               }
          |            ],
          |            "Severity": {
          |               "Code": null,
          |               "Codeset": null,
          |               "Name": null
          |            },
          |            "Status": null
          |         }
          |      ],
          |      "PCP": {
          |         "NPI": "4356789876",
          |         "ID": "4356789876",
          |         "IDType": "NPI",
          |         "FirstName": "Pat",
          |         "LastName": "Granite",
          |         "Credentials": [
          |            "MD"
          |         ],
          |         "Address": {
          |            "StreetAddress": "123 Main St.",
          |            "City": "Madison",
          |            "State": "WI",
          |            "ZIP": "53703",
          |            "County": "Dane",
          |            "Country": "USA"
          |         },
          |         "EmailAddresses": [],
          |         "PhoneNumber": {
          |            "Office": "+16085551234"
          |         },
          |         "Location": {
          |            "Type": null,
          |            "Facility": null,
          |            "Department": null,
          |            "Room": null
          |         }
          |      }
          |   },
          |   "Visit": {
          |      "VisitNumber": "1234",
          |      "AccountNumber": null,
          |      "PatientClass": "Inpatient",
          |      "VisitDateTime": "2021-08-20T16:58:51.935Z",
          |      "Duration": 15,
          |      "Reason": "Check up",
          |      "Instructions": [],
          |      "Balance": null,
          |      "DiagnosisRelatedGroup": null,
          |      "DiagnosisRelatedGroupType": null,
          |      "AttendingProvider": {
          |         "ID": "4356789876",
          |         "IDType": "NPI",
          |         "FirstName": "Pat",
          |         "LastName": "Granite",
          |         "Credentials": [
          |            "MD"
          |         ],
          |         "Address": {
          |            "StreetAddress": "123 Main St.",
          |            "City": "Madison",
          |            "State": "WI",
          |            "ZIP": "53703",
          |            "County": "Dane",
          |            "Country": "USA"
          |         },
          |         "EmailAddresses": [],
          |         "PhoneNumber": {
          |            "Office": "+16085551234"
          |         },
          |         "Location": {
          |            "Type": null,
          |            "Facility": null,
          |            "Department": null,
          |            "Room": null
          |         }
          |      },
          |      "ConsultingProvider": {
          |         "ID": null,
          |         "IDType": null,
          |         "FirstName": null,
          |         "LastName": null,
          |         "Credentials": [],
          |         "Address": {
          |            "StreetAddress": null,
          |            "City": null,
          |            "State": null,
          |            "ZIP": null,
          |            "County": null,
          |            "Country": null
          |         },
          |         "EmailAddresses": [],
          |         "PhoneNumber": {
          |            "Office": null
          |         },
          |         "Location": {
          |            "Type": null,
          |            "Facility": null,
          |            "Department": null,
          |            "Room": null
          |         }
          |      },
          |      "ReferringProvider": {
          |         "ID": null,
          |         "IDType": null,
          |         "FirstName": null,
          |         "LastName": null,
          |         "Credentials": [],
          |         "Address": {
          |            "StreetAddress": null,
          |            "City": null,
          |            "State": null,
          |            "ZIP": null,
          |            "County": null,
          |            "Country": null
          |         },
          |         "EmailAddresses": [],
          |         "PhoneNumber": {
          |            "Office": null
          |         },
          |         "Location": {
          |            "Type": null,
          |            "Facility": null,
          |            "Department": null,
          |            "Room": null
          |         }
          |      },
          |      "AdmittingProvider": {
          |         "ID": null,
          |         "IDType": null,
          |         "FirstName": null,
          |         "LastName": null,
          |         "Credentials": [],
          |         "Address": {
          |            "StreetAddress": null,
          |            "City": null,
          |            "State": null,
          |            "ZIP": null,
          |            "County": null,
          |            "Country": null
          |         },
          |         "EmailAddresses": [],
          |         "PhoneNumber": {
          |            "Office": null
          |         },
          |         "Location": {
          |            "Type": null,
          |            "Facility": null,
          |            "Department": null,
          |            "Room": null
          |         }
          |      },
          |      "AdditionalStaff": [],
          |      "Location": {
          |         "Type": "Inpatient",
          |         "Facility": "RES General Hospital",
          |         "Department": "3N",
          |         "Room": "136",
          |         "Bed": "B",
          |         "Address": {
          |            "StreetAddress": "9509 Integration Ln",
          |            "City": "Madison",
          |            "State": "WI",
          |            "ZIP": "53719",
          |            "County": "Dane",
          |            "Country": "US"
          |         }
          |      },
          |      "Guarantor": {
          |         "Number": "10001910",
          |         "FirstName": "Kent",
          |         "MiddleName": null,
          |         "LastName": "Bixby",
          |         "SSN": null,
          |         "DOB": null,
          |         "Sex": null,
          |         "Spouse": {
          |            "FirstName": "Barbara",
          |            "LastName": "Bixby"
          |         },
          |         "Address": {
          |            "StreetAddress": "4762 Hickory Street",
          |            "City": "Monroe",
          |            "State": "WI",
          |            "ZIP": "53566",
          |            "County": "Green",
          |            "Country": "USA"
          |         },
          |         "PhoneNumber": {
          |            "Home": null,
          |            "Business": null,
          |            "Mobile": null
          |         },
          |         "EmailAddresses": [],
          |         "Type": null,
          |         "RelationToPatient": "Father",
          |         "Employer": {
          |            "Name": "Accelerator Labs",
          |            "Address": {
          |               "StreetAddress": "1456 Old Sauk Road",
          |               "City": "Madison",
          |               "State": "WI",
          |               "ZIP": "53719",
          |               "County": "Dane",
          |               "Country": "USA"
          |            },
          |            "PhoneNumber": "+18083451121"
          |         }
          |      },
          |      "Insurances": [
          |         {
          |            "Plan": {
          |               "ID": "31572",
          |               "IDType": "Payor ID",
          |               "Name": "HMO Deductible Plan",
          |               "Type": null
          |            },
          |            "MemberNumber": null,
          |            "Company": {
          |               "ID": "60054",
          |               "IDType": null,
          |               "Name": "aetna (60054 0131)",
          |               "Address": {
          |                  "StreetAddress": "PO Box 14080",
          |                  "City": "Lexington",
          |                  "State": "KY",
          |                  "ZIP": "40512-4079",
          |                  "County": "Fayette",
          |                  "Country": "US"
          |               },
          |               "PhoneNumber": "+18089541123"
          |            },
          |            "GroupNumber": "847025-024-0009",
          |            "GroupName": "Accelerator Labs",
          |            "EffectiveDate": "2015-01-01",
          |            "ExpirationDate": "2020-12-31",
          |            "PolicyNumber": "9140860055",
          |            "Priority": null,
          |            "AgreementType": null,
          |            "CoverageType": null,
          |            "Insured": {
          |               "Identifiers": [],
          |               "LastName": null,
          |               "MiddleName": null,
          |               "FirstName": null,
          |               "SSN": null,
          |               "Relationship": null,
          |               "DOB": null,
          |               "Sex": null,
          |               "Address": {
          |                  "StreetAddress": null,
          |                  "City": null,
          |                  "State": null,
          |                  "ZIP": null,
          |                  "County": null,
          |                  "Country": null
          |               }
          |            }
          |         }
          |      ]
          |   }
          |}
        """.stripMargin

      val patientAdmin = validateJsonInput[PatientAdminMessage](json)

      // Check chart deserialization

      // Meta
      val meta = patientAdmin.Meta
      meta.DataModel must be equalTo DataModelTypes.PatientAdmin
      meta.EventType must be equalTo RedoxEventTypes.Arrival
      meta.Test.get must be equalTo true

      // Patient
      val patient = patientAdmin.Patient
      patient.Identifiers.size must be equalTo 3
      patient.Demographics must beSome
      patient.Demographics.get.EmailAddresses must not be empty
      patient.Demographics.get.EmailAddresses.size must be equalTo (1)
      patient.Demographics.get.EmailAddresses.head must be equalTo ("testfake12312@gmail.com")

      // Visit
      val visit = patientAdmin.Visit
      visit must beSome

      // Check request and response message
      val fut = client.post[PatientAdminMessage, EmptyResponse](patientAdmin)
      val maybe = handleResponse(fut)
      maybe must beSome
    }
  }
}
