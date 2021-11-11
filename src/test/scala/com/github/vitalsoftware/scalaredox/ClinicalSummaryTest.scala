package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.EmptyResponse
import com.github.vitalsoftware.scalaredox.models._
import com.github.vitalsoftware.scalaredox.models.clinicalsummary.{
  Demographics,
  Patient,
  PatientPush,
  PatientQuery,
  PatientQueryResponse
}
import org.joda.time.DateTime
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Created by apatzer on 3/23/17.
 */
class ClinicalSummaryTest extends Specification with RedoxTest {
  "query PatientQuery" should {
    "return an error" in {
      val shouldFailQuery = PatientQuery(
        Meta(DataModel = DataModelTypes.ClinicalSummary, EventType = RedoxEventTypes.Query),
        Patient(
          Identifiers = Seq.empty,
          Demographics = Some(Demographics("John", None, "Doe", DateTime.parse("1970-1-1"), Sex = SexType.Male))
        )
      )
      val fut = client.get[PatientQuery, PatientQueryResponse](shouldFailQuery)
      val resp = Await.result(fut, timeout)
      resp.isError must beTrue
      resp.get must throwA[Exception]
      resp.getError.Errors must not be empty
    }

    "return a PatientQueryResponse" in {
      val json =
        """
          |{
          |	"Meta": {
          |		"DataModel": "Clinical Summary",
          |		"EventType": "PatientQuery",
          |		"EventDateTime": "2017-03-14T19:35:06.047Z",
          |		"Test": true,
          |		"Destinations": [
          |			{
          |				"ID": "ef9e7448-7f65-4432-aa96-059647e9b357",
          |				"Name": "Clinical Summary Endpoint"
          |			}
          |		]
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
          |		]
          |	}
          |}
        """.stripMargin

      val query = validateJsonInput[PatientQuery](json)
      val fut = client.get[PatientQuery, PatientQueryResponse](query)
      val maybe = handleResponse(fut)
      maybe must beSome
      maybe.map { clinicalSummary =>
        // Meta
        val meta = clinicalSummary.Meta
        meta.EventType must be equalTo RedoxEventTypes.PatientQuery
        meta.Destinations must not be empty

        // Vital Signs
        val vs = clinicalSummary.VitalSigns
        vs must not be empty
        vs.head.Observations.size must be_>=(3)

        // Social History
        val sh = clinicalSummary.SocialHistory
        sh must beSome
        sh.get.TobaccoUse must not be empty

        // Problems
        val pr = clinicalSummary.Problems
        pr must not be empty
        pr.size must be_>(3)
        pr.head.Status must beSome

        // Plan of Care
        val pc = clinicalSummary.PlanOfCare
        pc must beSome
        pc.get.Orders must not be empty

        // Medications
        val meds = clinicalSummary.Medications
        meds.size must be_>(10)
        meds.head.Dose must beSome
        meds.head.Route must beSome

        // Family History
        val fh = clinicalSummary.FamilyHistory
        fh must not be empty
        fh.head.Problems must not be empty

        // Header
        val header = clinicalSummary.Header
        header.Patient.Identifiers must not be empty
        header.Patient.Demographics must beSome
      }.get
    }

    "return a VisitQueryResponse" in {
      val json =
        """
          |{
          |  "Meta": {
          |    "DataModel": "Clinical Summary",
          |    "EventType": "VisitQuery",
          |    "Test": true,
          |    "Source": {
          |      "ID": "00000000-0000-0000-0000-000000000000",
          |      "Name": "Vital ER"
          |    },
          |    "Destinations": [
          |      {
          |        "ID": "ef9e7448-7f65-4432-aa96-059647e9b357"
          |      }
          |    ]
          |  },
          |  "Patient": {
          |    "Identifiers": [
          |      {
          |        "ID": "a1d4ee8aba494ca",
          |        "IDType": "NIST"
          |      }
          |    ]
          |  },
          |  "Visit": {
          |    "StartDateTime": "2017-04-30T19:25:08.415+08:00",
          |    "EndDateTime": "2017-05-29T19:25:08.415+08:00"
          |  }
          |}
        """.stripMargin

      val query = validateJsonInput[VisitQuery](json)
      val fut = client.get[VisitQuery, Visit](query)
      val maybe = handleResponse(fut)
      maybe must beSome
      maybe.map { visit =>
        // Meta
        val meta = visit.Meta
        meta.EventType must be equalTo RedoxEventTypes.VisitQuery
        meta.Destinations must not be empty

        // Vital Signs
        val vs = visit.VitalSigns
        vs must not be empty
        vs.head.Observations.size must be_>=(3)

        // Problems
        val pr = visit.Problems
        pr must not be empty
        pr.size must be_>(1)
        pr.head.Status must beSome

        // Plan of Care
        val pc = visit.PlanOfCare
        pc must beSome
        pc.get.Orders must not be empty

        // Medications
        val meds = visit.Medications
        meds.size must be_>(1)
        meds.head.Dose must beSome
        meds.head.Route must beSome

        // Header
        val header = visit.Header
        header.Patient.Identifiers must not be empty
        header.Patient.Demographics must beSome

        // TODO: {Allergies, Assessment, Encounters, Results} No longer returned in the test response from Redox!

        // Allergies
        //val allergies = visitQueryResponse.Allergies
        //allergies.size must be_>(2)

        // Assessment
        //val assesment = visitQueryResponse.Assessment
        //assesment must beSome
        //assesment.head.Diagnoses.size must be_>(1)

        // Encounters
        //val encounters = visitQueryResponse.Encounters
        //encounters.size must be_>(0)

        // Results
        //val results = visitQueryResponse.Results
        //results.size must be_>(0)
      }.get
    }
  }

  "post PatientPush" should {
    "return OK" in {
      val json =
        """
          |{
          |	"Meta": {
          |		"DataModel": "Clinical Summary",
          |		"EventType": "PatientPush",
          |		"EventDateTime": "2017-03-14T19:35:06.047Z",
          |		"Test": true,
          |		"Source": {
          |			"ID": "7ce6f387-c33c-417d-8682-81e83628cbd9",
          |			"Name": "Redox Dev Tools"
          |		},
          |		"Destinations": [
          |			{
          |				"ID": "ef9e7448-7f65-4432-aa96-059647e9b358",
          |				"Name": "Clinical Summary Push Endpoint"
          |			}
          |		],
          |		"Message": {
          |			"ID": 5565
          |		},
          |		"Transmission": {
          |			"ID": 12414
          |		}
          |	},
          |	"Header": {
          |		"Document": {
          |			"ID": "75cb4ad4-e5f9-4cd3-8750-eb5050521e0d",
          |			"Author": {
          |				"ID": "4356789876",
          |				"IDType": "NPI",
          |				"Type": null,
          |				"FirstName": "Pat",
          |				"LastName": "Granite",
          |				"Credentials": [
          |					"MD"
          |				],
          |				"Address": {
          |					"StreetAddress": "123 Main St.",
          |					"City": "Madison",
          |					"State": "WI",
          |					"ZIP": "53703",
          |					"County": "Dane",
          |					"Country": "USA"
          |				},
          |				"Location": {
          |					"Type": null,
          |					"Facility": null,
          |					"Department": null
          |				},
          |				"PhoneNumber": {
          |					"Office": "+16085551234"
          |				}
          |			},
          |			"Visit": {
          |				"Location": {
          |					"Type": "Inpatient",
          |					"Facility": "RES General Hospital",
          |					"Department": "3N"
          |				},
          |				"StartDateTime": "2017-03-15T00:35:26.713Z",
          |				"Reason": "Annual Physical",
          |				"EndDateTime": "2017-03-15T00:35:26.713Z"
          |			},
          |			"Locale": "US",
          |			"Title": "Community Health and Hospitals: Health Summary",
          |			"DateTime": "2012-09-12T00:00:00.000Z",
          |			"Type": "Summarization of Episode Note"
          |         "Extensions": {
          |            "url": "https://api.redoxengine.com/extensions/file-contents",
          |            "string": "filename.xml"
          |         }
          |		},
          |		"Patient": {
          |			"Identifiers": [
          |				{
          |					"ID": "0000000001",
          |					"IDType": "MR",
          |					"Type": null
          |				},
          |				{
          |					"ID": "e167267c-16c9-4fe3-96ae-9cff5703e90a",
          |					"IDType": "EHRID",
          |					"Type": null
          |				},
          |				{
          |					"ID": "a1d4ee8aba494ca^^^&1.3.6.1.4.1.21367.2005.13.20.1000&ISO",
          |					"IDType": "NIST",
          |					"Type": null
          |				}
          |			],
          |			"Demographics": {
          |				"FirstName": "Timothy",
          |				"LastName": "Bixby",
          |				"DOB": "2008-01-06",
          |				"SSN": "101-01-0001",
          |				"Sex": "Male",
          |				"Address": {
          |					"StreetAddress": "4762 Hickory Street",
          |					"City": "Monroe",
          |					"State": "WI",
          |					"County": "Green",
          |					"Country": "US",
          |					"ZIP": "53566"
          |				},
          |				"PhoneNumber": {
          |					"Home": "+18088675301",
          |					"Mobile": null
          |				},
          |				"EmailAddresses": [
          |               {
          |                 "Address": "12313124@fake.com"
          |               }
          |             ],
          |				"Race": "Asian",
          |				"IsHispanic": null,
          |				"Religion": null,
          |				"MaritalStatus": "Single"
          |			}
          |		}
          |	},
          |	"AdvanceDirectives": [
          |		{
          |			"Type": {
          |				"Code": "304251008",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Resuscitation"
          |			},
          |			"Code": "304253006",
          |			"CodeSystem": "2.16.840.1.113883.6.96",
          |			"CodeSystemName": "SNOMED CT",
          |			"Name": "Do not resuscitate",
          |			"StartDate": "2011-02-13T05:00:00.000Z",
          |			"EndDate": null,
          |			"ExternalReference": "AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf",
          |			"VerifiedBy": [
          |				{
          |					"FirstName": "Robert",
          |					"LastName": "Dolin",
          |					"Credentials": "Dr.",
          |					"DateTime": null
          |				}
          |			],
          |			"Custodians": [
          |				{
          |					"FirstName": "Robert",
          |					"LastName": "Dolin",
          |					"Credentials": "Dr.",
          |					"Address": {
          |						"StreetAddress": "21 North Ave.",
          |						"City": "Burlington",
          |						"State": "MA",
          |						"Country": "USA",
          |						"ZIP": "02368"
          |					}
          |				}
          |			]
          |		}
          |	],
          |	"Allergies": [
          |		{
          |			"Type": {
          |				"Code": "419511003",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Propensity to adverse reaction to drug"
          |			},
          |			"Substance": {
          |				"Code": "7982",
          |				"CodeSystem": "2.16.840.1.113883.6.88",
          |				"CodeSystemName": "RxNorm",
          |				"Name": "Penicillin G benzathine"
          |			},
          |			"Reaction": [
          |				{
          |					"Code": "28926001",
          |					"CodeSystem": "2.16.840.1.113883.6.96",
          |					"CodeSystemName": "SNOMED CT",
          |					"Name": "Rash",
          |					"Text": null
          |				},
          |				{
          |					"Code": "247472004",
          |					"CodeSystem": "2.16.840.1.113883.6.96",
          |					"CodeSystemName": "SNOMED CT",
          |					"Name": "Hives",
          |					"Text": null
          |				}
          |			],
          |			"Severity": {
          |				"Code": "371924009",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Moderate to severe"
          |			},
          |			"Status": {
          |				"Code": "73425007",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Inactive"
          |			},
          |			"StartDate": null,
          |			"EndDate": null
          |		},
          |		{
          |			"Type": {
          |				"Code": "419511003",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Propensity to adverse reaction to drug"
          |			},
          |			"Substance": {
          |				"Code": "2670",
          |				"CodeSystem": "2.16.840.1.113883.6.88",
          |				"CodeSystemName": "RxNorm",
          |				"Name": "Codeine"
          |			},
          |			"Reaction": [
          |				{
          |					"Code": "267036007",
          |					"CodeSystem": "2.16.840.1.113883.6.96",
          |					"CodeSystemName": "SNOMED CT",
          |					"Name": "Shortness of Breath",
          |					"Text": null
          |				}
          |			],
          |			"Severity": {
          |				"Code": "6736007",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Moderate"
          |			},
          |			"Status": {
          |				"Code": "55561003",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Active"
          |			},
          |			"StartDate": null,
          |			"EndDate": null
          |		},
          |		{
          |			"Type": {
          |				"Code": "419511003",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Propensity to adverse reaction to drug"
          |			},
          |			"Substance": {
          |				"Code": "1191",
          |				"CodeSystem": "2.16.840.1.113883.6.88",
          |				"CodeSystemName": "RxNorm",
          |				"Name": "Aspirin"
          |			},
          |			"Reaction": [
          |				{
          |					"Code": "247472004",
          |					"CodeSystem": "2.16.840.1.113883.6.96",
          |					"CodeSystemName": "SNOMED CT",
          |					"Name": "Hives",
          |					"Text": null
          |				}
          |			],
          |			"Severity": {
          |				"Code": "371923003",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Mild to moderate"
          |			},
          |			"Status": {
          |				"Code": "55561003",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Active"
          |			},
          |			"StartDate": null,
          |			"EndDate": null
          |		}
          |	],
          |	"Encounters": [
          |		{
          |  "Identifiers": [
          |        {
          |           "ID": "2376492",
          |           "IDType": "URMC Epic CSN"
          |        },
          |        {
          |           "ID": "8237334",
          |           "IDType": "1.35.829.5.238422.9.10"
          |        }
          |     ],
          |			"Type": {
          |				"Code": "99222",
          |				"CodeSystem": "2.16.840.1.113883.6.12",
          |				"CodeSystemName": "CPT",
          |				"Name": "InPatient Admission"
          |			},
          |			"DateTime": "2012-08-06T04:00:00.000Z",
          |			"EndDateTime": null,
          |			"Providers": [
          |				{
          |					"ID": null,
          |					"IDType": null,
          |					"FirstName": null,
          |					"LastName": null,
          |					"Credentials": [],
          |					"Address": {
          |						"StreetAddress": null,
          |						"City": null,
          |						"State": null,
          |						"ZIP": null,
          |						"County": null,
          |						"Country": null
          |					},
          |					"Location": {
          |						"Type": null,
          |						"Facility": null,
          |						"Department": null
          |					},
          |					"PhoneNumber": {
          |						"Office": null
          |					},
          |					"Role": {
          |						"Code": "59058001",
          |						"CodeSystem": "2.16.840.1.113883.6.96",
          |						"CodeSystemName": "SNOMED CT",
          |						"Name": "General Physician"
          |					}
          |				}
          |			],
          |			"Locations": [
          |				{
          |					"Address": {
          |						"StreetAddress": "1002 Healthcare Dr",
          |						"City": "Portland",
          |						"State": "OR",
          |						"Country": "US",
          |						"ZIP": "97266"
          |					},
          |					"Type": {
          |						"Code": "1160-1",
          |						"CodeSystem": "2.16.840.1.113883.6.259",
          |						"CodeSystemName": "HealthcareServiceLocation",
          |						"Name": "Urgent Care Center"
          |					},
          |					"Name": "Community Health and Hospitals"
          |				}
          |			],
          |			"Diagnosis": [
          |				{
          |					"Code": "233604007",
          |					"CodeSystem": "2.16.840.1.113883.6.96",
          |					"CodeSystemName": "SNOMED CT",
          |					"Name": "Pneumonia"
          |				}
          |			],
          |			"ReasonForVisit": [
          |				{
          |					"Code": "233604007",
          |					"CodeSystem": "2.16.840.1.113883.6.96",
          |					"CodeSystemName": "SNOMED CT",
          |					"Name": "Pneumonia"
          |				}
          |			]
          |		}
          |	],
          |	"FamilyHistory": [
          |		{
          |			"Relation": {
          |				"Code": "FTH",
          |				"CodeSystem": "2.16.840.1.113883.5.111",
          |				"CodeSystemName": "HL7 FamilyMember",
          |				"Name": "Father",
          |				"Demographics": {
          |					"Sex": "Male",
          |					"DOB": "1912-01-01"
          |				},
          |				"IsDeceased": true
          |			},
          |			"Problems": [
          |				{
          |					"Code": "22298006",
          |					"CodeSystem": "2.16.840.1.113883.6.96",
          |					"CodeSystemName": "SNOMED CT",
          |					"Name": "Myocardial infarction",
          |					"Type": {
          |						"Code": "55561003",
          |						"CodeSystem": "2.16.840.1.113883.6.96",
          |						"CodeSystemName": "SNOMED CT",
          |						"Name": "Active"
          |					},
          |					"DateTime": null,
          |					"AgeAtOnset": "57",
          |					"IsCauseOfDeath": null
          |				},
          |				{
          |					"Code": "46635009",
          |					"CodeSystem": "2.16.840.1.113883.6.96",
          |					"CodeSystemName": "SNOMED CT",
          |					"Name": "Diabetes mellitus type 1",
          |					"Type": {
          |						"Code": "7087005",
          |						"CodeSystem": "2.16.840.1.113883.6.96",
          |						"CodeSystemName": "SNOMED CT",
          |						"Name": "Intermittent"
          |					},
          |					"DateTime": "1994-01-01T05:00:00.000Z",
          |					"AgeAtOnset": "40",
          |					"IsCauseOfDeath": null
          |				}
          |			]
          |		}
          |	],
          |	"Immunizations": [
          |		{
          |			"DateTime": "2012-05-10T04:00:00.000Z",
          |			"Route": {
          |				"Code": "C28161",
          |				"CodeSystem": "2.16.840.1.113883.3.26.1.1",
          |				"CodeSystemName": "National Cancer Institute (NCI) Thesaurus",
          |				"Name": "Intramuscular injection"
          |			},
          |			"Product": {
          |				"Manufacturer": "Health LS - Immuno Inc.",
          |				"Code": "88",
          |				"CodeSystem": "2.16.840.1.113883.6.59",
          |				"CodeSystemName": "CVX",
          |				"Name": "Influenza virus vaccine",
          |				"LotNumber": null
          |			},
          |			"Dose": {
          |				"Quantity": "50",
          |				"Units": "mcg"
          |			}
          |		},
          |		{
          |			"DateTime": "2012-04-01T04:00:00.000Z",
          |			"Route": {
          |				"Code": "C28161",
          |				"CodeSystem": "2.16.840.1.113883.3.26.1.1",
          |				"CodeSystemName": "National Cancer Institute (NCI) Thesaurus",
          |				"Name": "Intramuscular injection"
          |			},
          |			"Product": {
          |				"Manufacturer": "Health LS - Immuno Inc.",
          |				"Code": "103",
          |				"CodeSystem": "2.16.840.1.113883.6.59",
          |				"CodeSystemName": "CVX",
          |				"Name": "Tetanus and diphtheria toxoids - preservative free",
          |				"LotNumber": null
          |			},
          |			"Dose": {
          |				"Quantity": "50",
          |				"Units": "mcg"
          |			}
          |		}
          |	],
          |	"MedicalEquipment": [
          |		{
          |			"Status": "completed",
          |			"StartDate": "1999-11-01T05:00:00.000Z",
          |			"Quantity": "2",
          |			"Product": {
          |				"Code": "72506001",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Automatic implantable cardioverter/defibrillator"
          |			}
          |		},
          |		{
          |			"Status": "completed",
          |			"StartDate": "1998-01-01T05:00:00.000Z",
          |			"Quantity": null,
          |			"Product": {
          |				"Code": "304120007",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Total hip replacement prosthesis"
          |			}
          |		},
          |		{
          |			"Status": "completed",
          |			"StartDate": "1999-01-01T05:00:00.000Z",
          |			"Quantity": null,
          |			"Product": {
          |				"Code": "58938008",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Wheelchair"
          |			}
          |		}
          |	],
          |	"Medications": [
          |		{
          |			"Prescription": false,
          |			"FreeTextSig": null,
          |			"Dose": {
          |				"Quantity": "4",
          |				"Units": "mg"
          |			},
          |			"Rate": {
          |				"Quantity": null,
          |				"Units": null
          |			},
          |			"Route": {
          |				"Code": "C38288",
          |				"CodeSystem": "2.16.840.1.113883.3.26.1.1",
          |				"CodeSystemName": "NCI Thesaurus",
          |				"Name": "Oral"
          |			},
          |         "Status": "active",
          |			"StartDate": "2013-11-11T05:00:00.000Z",
          |			"EndDate": null,
          |			"Frequency": {
          |				"Period": "8",
          |				"Unit": "h"
          |			},
          |         "NumberOfRefillsRemaining": 2,
          |			"Product": {
          |				"Code": "104894",
          |				"CodeSystem": "2.16.840.1.113883.6.88",
          |				"CodeSystemName": "RxNorm",
          |				"Name": "Ondansetron 4 Mg Po Tbdp"
          |			},
          |         "Extensions": {
          |             "Indication": {
          |                 "url": "https://api.redoxengine.com/extensions/indication",
          |                 "coding": {
          |                     "system": "2.16.840.1.113883.6.90",
          |                     "code": "J18.1",
          |                     "display": "Lobar pneumonia"
          |                 }
          |             },
          |             "author-id": {
          |                 "url": "https://api.redoxengine.com/extensions/author-id",
          |                 "string": "7236481726"
          |             },
          |             "author-name": {
          |                 "url": "https://api.redoxengine.com/extensions/author-name",
          |                 "humanName": {
          |                     "use": "usual",
          |                     "text": "Billy",
          |                     "given": [
          |                         "Billy"
          |                     ]
          |                 }
          |             }
          |		    }
          |     },
          |		{
          |			"Prescription": false,
          |			"FreeTextSig": null,
          |			"Dose": {
          |				"Quantity": "0.09",
          |				"Units": "mg/actuat"
          |			},
          |			"Rate": {
          |				"Quantity": "90",
          |				"Units": "ml/min"
          |			},
          |			"Route": {
          |				"Code": "C38216",
          |				"CodeSystem": "2.16.840.1.113883.3.26.1.1",
          |				"CodeSystemName": "NCI Thesaurus",
          |				"Name": "RESPIRATORY (INHALATION)"
          |			},
          |         "Status": "completed",
          |			"StartDate": "2012-08-06T04:00:00.000Z",
          |			"EndDate": "2012-08-13T04:00:00.000Z",
          |			"Frequency": {
          |				"Period": "12",
          |				"Unit": "h"
          |			},
          |         "NumberOfRefillsRemaining": 1,
          |			"Product": {
          |				"Code": "573621",
          |				"CodeSystem": "2.16.840.1.113883.6.88",
          |				"CodeSystemName": "RxNorm",
          |				"Name": "Albuterol 0.09 MG/ACTUAT inhalant solution"
          |			},
          |        "Extensions": {
          |         "Indication": {
          |             "url": "https://api.redoxengine.com/extensions/indication",
          |             "coding": {
          |                 "system": "2.16.840.1.113883.6.90",
          |                 "code": "J18.1",
          |                 "display": "Lobar pneumonia"
          |             }
          |         },
          |         "author-id": {
          |             "url": "https://api.redoxengine.com/extensions/author-id",
          |             "string": "7236481726"
          |         },
          |         "author-name": {
          |             "url": "https://api.redoxengine.com/extensions/author-name",
          |             "humanName": {
          |                 "use": "usual",
          |                 "text": "Billy",
          |                 "given": [
          |                     "Billy"
          |                 ]
          |             }
          |         }
          |		}
          |	],
          |	"PlanOfCare": {
          |		"Orders": [
          |			{
          |				"Code": "624-7",
          |				"CodeSystem": "2.16.840.1.113883.6.1",
          |				"CodeSystemName": null,
          |				"Name": "Sputum Culture",
          |				"Status": "Request",
          |				"DateTime": "2012-08-20T05:00:00.000Z"
          |			}
          |		],
          |		"Procedures": [
          |			{
          |				"Code": "168731009",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED-CT",
          |				"Name": "Chest X-Ray",
          |				"Status": "Request",
          |				"DateTime": "2012-08-26T05:00:00.000Z"
          |			}
          |		],
          |		"Encounters": [
          |			{
          |				"Code": "99241",
          |				"CodeSystem": "2.16.840.1.113883.6.12",
          |				"CodeSystemName": "CPT",
          |				"Name": "Office consultation - 15 minutes",
          |				"Status": "Intent",
          |				"DateTime": "2012-08-20T05:00:00.000Z"
          |			}
          |		],
          |		"MedicationAdministration": [
          |			{
          |				"Status": "Intent",
          |				"Dose": {
          |					"Quantity": "81",
          |					"Units": "milliGRAM(s)"
          |				},
          |				"Rate": {
          |					"Quantity": null,
          |					"Units": null
          |				},
          |				"Route": {
          |					"Code": "C38288",
          |					"CodeSystem": "2.16.840.1.113883.3.26.1.1",
          |					"CodeSystemName": "NCI Thesaurus",
          |					"Name": "ORAL"
          |				},
          |				"StartDate": "2012-10-02T05:00:00.000Z",
          |				"EndDate": "2012-10-31T04:59:00.000Z",
          |				"Frequency": {
          |					"Period": null,
          |					"Unit": null
          |				},
          |				"Product": {
          |					"Code": "1191",
          |					"CodeSystem": "2.16.840.1.113883.6.88",
          |					"CodeSystemName": "RxNorm",
          |					"Name": "aspirin"
          |				}
          |			}
          |		],
          |		"Supplies": [],
          |		"Services": [
          |			{
          |				"Code": "427519008",
          |				"CodeSystem": "2.16.840.1.113883.11.20.9.34",
          |				"CodeSystemName": "patientEducationType",
          |				"Name": "Caregiver",
          |				"Status": "Intent",
          |				"DateTime": null
          |			}
          |		]
          |	},
          |	"Problems": [
          |		{
          |			"StartDate": "2012-08-06T04:00:00.000Z",
          |			"EndDate": "2012-08-06T04:00:00.000Z",
          |			"Code": "233604007",
          |			"CodeSystem": "2.16.840.1.113883.6.96",
          |			"CodeSystemName": "SNOMED CT",
          |			"Name": "Pneumonia",
          |			"Category": {
          |				"Code": "409586006",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Complaint"
          |			},
          |			"HealthStatus": {
          |				"Code": "162467007",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Symptom Free"
          |			},
          |			"Status": {
          |				"Code": "413322009",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Resolved"
          |			}
          |		},
          |		{
          |			"StartDate": "2007-10-17T04:00:00.000Z",
          |			"EndDate": "2012-08-06T04:00:00.000Z",
          |			"Code": "195967001",
          |			"CodeSystem": "2.16.840.1.113883.6.96",
          |			"CodeSystemName": "SNOMED CT",
          |			"Name": "Asthma",
          |			"Category": {
          |				"Code": "409586006",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Complaint"
          |			},
          |			"HealthStatus": {
          |				"Code": "162467007",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Symptom Free"
          |			},
          |			"Status": {
          |				"Code": "55561003",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Active"
          |			}
          |		}
          |	],
          |	"Procedures": {
          |		"Observations": [
          |			{
          |				"Code": "123456",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED-CT",
          |				"Name": "Fake observation",
          |				"DateTime": "20120807",
          |				"Status": "active",
          |				"TargetSite": {
          |					"Code": "2.16.840.1.113883.6.96",
          |					"CodeSystem": "2.16.840.1.113883.6.96",
          |					"CodeSystemName": "SNOMED-CT",
          |					"Name": "Entire hand (body structure)"
          |				}
          |			}
          |		],
          |		"Procedures": [
          |			{
          |				"Code": "168731009",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED-CT",
          |				"Name": "Chest X-Ray",
          |				"DateTime": "20120807",
          |				"Status": "completed"
          |			}
          |		],
          |		"Services": [
          |			{
          |				"Code": "123456",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED-CT",
          |				"Name": "Fake action",
          |				"DateTime": "20120807",
          |				"Status": "aborted"
          |			}
          |		]
          |	},
          |	"Results": [
          |		{
          |			"Code": "43789009",
          |			"CodeSystem": "2.16.840.1.113883.6.96",
          |			"CodeSystemName": "SNOMED CT",
          |			"Name": "CBC WO DIFFERENTIAL",
          |			"Status": "Final",
          |			"Observations": [
          |				{
          |					"Code": "30313-1",
          |					"CodeSystem": "2.16.840.1.113883.6.1",
          |					"CodeSystemName": "LOINC",
          |					"Name": "HGB",
          |					"Status": "Final",
          |					"Interpretation": "Normal",
          |					"DateTime": "2012-08-10T14:00:00.000Z",
          |					"Value": "10.2",
          |                 "ValueType": "PhysicalQuantity",
          |					"Units": "g/dl",
          |					"ReferenceRange": {
          |						"Low": null,
          |						"High": null,
          |						"Text": null
          |					}
          |				},
          |				{
          |					"Code": "33765-9",
          |					"CodeSystem": "2.16.840.1.113883.6.1",
          |					"CodeSystemName": "LOINC",
          |					"Name": "WBC",
          |					"Status": "Final",
          |					"Interpretation": "Normal",
          |					"DateTime": "2012-08-10T14:00:00.000Z",
          |					"Value": "12.3",
          |                 "ValueType": "PhysicalQuantity",
          |					"Units": "10+3/ul",
          |					"ReferenceRange": {
          |						"Low": null,
          |						"High": null,
          |						"Text": null
          |					}
          |				},
          |				{
          |					"Code": "26515-7",
          |					"CodeSystem": "2.16.840.1.113883.6.1",
          |					"CodeSystemName": "LOINC",
          |					"Name": "PLT",
          |					"Status": "Final",
          |					"Interpretation": "Low",
          |					"DateTime": "2012-08-10T14:00:00.000Z",
          |					"Value": "123",
          |                 "ValueType": "PhysicalQuantity",
          |					"Units": "10+3/ul",
          |					"ReferenceRange": {
          |						"Low": null,
          |						"High": null,
          |						"Text": null
          |					}
          |				}
          |			]
          |		}
          |	],
          |	"SocialHistory": {
          |		"Observations": [
          |			{
          |				"Code": "160573003",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Alcohol Consumption",
          |				"Value": {
          |					"Code": null,
          |					"CodeSystem": null,
          |					"CodeSystemName": null,
          |					"Name": null
          |				},
          |				"ValueText": "None",
          |				"StartDate": "1990-05-01T04:00:00.000Z",
          |				"EndDate": null
          |			}
          |		],
          |		"Pregnancy": [],
          |		"TobaccoUse": [
          |			{
          |				"Code": "8517006",
          |				"CodeSystem": "2.16.840.1.113883.6.96",
          |				"CodeSystemName": "SNOMED CT",
          |				"Name": "Former smoker",
          |				"StartDate": "2005-05-01T04:00:00.000Z",
          |				"EndDate": "2011-02-27T05:00:00.000Z"
          |			}
          |		]
          |	},
          |	"VitalSigns": [
          |		{
          |			"DateTime": "1999-11-14T00:00:00.000Z",
          |			"Observations": [
          |				{
          |					"Code": "8302-2",
          |					"CodeSystem": "2.16.840.1.113883.6.1",
          |					"CodeSystemName": "LOINC",
          |					"Name": "Height",
          |					"Status": "completed",
          |					"Interpretation": "Normal",
          |					"DateTime": "1999-11-14T00:00:00.000Z",
          |					"Value": "177",
          |					"Units": "cm"
          |				},
          |				{
          |					"Code": "3141-9",
          |					"CodeSystem": "2.16.840.1.113883.6.1",
          |					"CodeSystemName": "LOINC",
          |					"Name": "Patient Body Weight - Measured",
          |					"Status": "completed",
          |					"Interpretation": "Normal",
          |					"DateTime": "1999-11-14T00:00:00.000Z",
          |					"Value": "86",
          |					"Units": "kg"
          |				},
          |				{
          |					"Code": "8480-6",
          |					"CodeSystem": "2.16.840.1.113883.6.1",
          |					"CodeSystemName": "LOINC",
          |					"Name": "Intravascular Systolic",
          |					"Status": "completed",
          |					"Interpretation": "Normal",
          |					"DateTime": "1999-11-14T00:00:00.000Z",
          |					"Value": "132",
          |					"Units": "mm[Hg]"
          |				}
          |			]
          |		},
          |		{
          |			"DateTime": "2000-04-07T00:00:00.000Z",
          |			"Observations": [
          |				{
          |					"Code": "8302-2",
          |					"CodeSystem": "2.16.840.1.113883.6.1",
          |					"CodeSystemName": "LOINC",
          |					"Name": "Height",
          |					"Status": "completed",
          |					"Interpretation": "Normal",
          |					"DateTime": "2000-04-07T00:00:00.000Z",
          |					"Value": "177",
          |					"Units": "cm"
          |				},
          |				{
          |					"Code": "3141-9",
          |					"CodeSystem": "2.16.840.1.113883.6.1",
          |					"CodeSystemName": "LOINC",
          |					"Name": "Patient Body Weight - Measured",
          |					"Status": "completed",
          |					"Interpretation": "Normal",
          |					"DateTime": "2000-04-07T00:00:00.000Z",
          |					"Value": "88",
          |					"Units": "kg"
          |				},
          |				{
          |					"Code": "8480-6",
          |					"CodeSystem": "2.16.840.1.113883.6.1",
          |					"CodeSystemName": "LOINC",
          |					"Name": "Intravascular Systolic",
          |					"Status": "completed",
          |					"Interpretation": "Normal",
          |					"DateTime": "2000-04-07T00:00:00.000Z",
          |					"Value": "145",
          |					"Units": "mm[Hg]"
          |				}
          |			]
          |		}
          |	]
          |}
        """.stripMargin

      val patientPush = validateJsonInput[PatientPush](json)

      // Check chart deserialization

      // Meta
      val meta = patientPush.Meta
      meta.EventType must be equalTo RedoxEventTypes.PatientPush
      meta.Destinations must not be empty

      // Header
      val h = patientPush.Header
      h.Document.Author must beSome
      h.Document.Author.flatMap(_.Address) must beSome
      h.Document.Visit must beSome
      h.Document.Extensions must beSome
      h.Patient.Identifiers must not be empty
      h.Patient.Demographics must beSome
      h.Patient.Demographics.flatMap(_.Address) must beSome
      h.Patient.Demographics.flatMap(_.PhoneNumber) must beSome
      h.Patient.Demographics.get.EmailAddresses must not be empty
      h.Patient.Demographics.get.EmailAddresses.size must be equalTo (1)
      h.Patient.Demographics.get.EmailAddresses.head.Address.get must be equalTo ("12313124@fake.com")

      // Collections
      patientPush.AdvanceDirectives must not be empty
      patientPush.Allergies must not be empty
      patientPush.Encounters must not be empty
      patientPush.FamilyHistory must not be empty
      patientPush.Immunizations must not be empty
      patientPush.MedicalEquipment must not be empty
      patientPush.Medications must not be empty
      patientPush.Medications.head.Status must be(Some("active"))
      patientPush.Medications.head.NumberOfRefillsRemaining must be(Some(2))
      patientPush.Medications.head.Extensions.get.Indication.get.coding.system must be("2.16.840.1.113883.6.90")
      patientPush.Medications.head.Extensions.get.Indication.get.coding.code must be("J18.1")
      patientPush.Medications.head.Extensions.get.Indication.get.coding.display must be("Lobar pneumonia")
      patientPush.Medications.head.Extensions.get.`author-id`.get.string must be("7236481726")
      patientPush.Medications.head.Extensions.get.`author-name`.get.humanName.use must be("usual")
      patientPush.Medications.head.Extensions.get.`author-name`.get.humanName.text must be("Billy")
      patientPush.Medications.head.Extensions.get.`author-name`.get.humanName.`given` must be(Seq("Billy"))
      patientPush.Medications(1).Status must be(Some("completed"))
      patientPush.Medications(1).NumberOfRefillsRemaining must be(Some(1))
      patientPush.Medications(1).Extensions must beSome
      patientPush.PlanOfCare must beSome
      patientPush.PlanOfCare.get.Encounters must not be empty
      patientPush.PlanOfCare.get.MedicationAdministration must not be empty
      patientPush.PlanOfCare.get.Orders must not be empty
      patientPush.PlanOfCare.get.Procedures must not be empty
      patientPush.PlanOfCare.get.Services must not be empty
      patientPush.Problems must not be empty
      patientPush.Procedures must beSome
      patientPush.Procedures.get.Observations must not be empty
      patientPush.Procedures.get.Procedures must not be empty
      patientPush.Procedures.get.Services must not be empty
      patientPush.Results must not be empty
      patientPush.Results.head.Observations must not be empty
      patientPush.Results.head.Observations.head.ValueType.get must be equalTo (ValueTypes.PhysicalQuantity)
      patientPush.Results.head.Observations(1).ValueType.get must be equalTo (ValueTypes.PhysicalQuantity)
      patientPush.Results.head.Observations(2).ValueType.get must be equalTo (ValueTypes.PhysicalQuantity)
      patientPush.SocialHistory must beSome
      patientPush.SocialHistory.get.Observations must not be empty
      patientPush.SocialHistory.get.TobaccoUse must not be empty
      patientPush.VitalSigns must not be empty
      patientPush.VitalSigns.head.Observations must not be empty

      // Check request and response message

      val fut = client.post[PatientPush, EmptyResponse](patientPush)
      val maybe = handleResponse(fut)
      maybe must beSome
    }.pendingUntilFixed
  }
}
