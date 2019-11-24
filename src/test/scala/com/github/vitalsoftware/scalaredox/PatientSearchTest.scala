package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.models._
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Created by apatzer on 3/23/17.
 */
class PatientSearchTest extends Specification with RedoxTest {
  "query PatientSearch" should {
    "not find anyone" in {
      val json =
        """
          |{
          |	"Meta": {
          |		"DataModel": "PatientSearch",
          |		"EventType": "Query",
          |		"EventDateTime": "2017-03-14T19:35:06.047Z",
          |		"Test": true,
          |		"Destinations": [
          |			{
          |				"ID": "0f4bd1d1-451d-4351-8cfd-b767d1b488d6",
          |				"Name": "Patient Search Endpoint"
          |			}
          |		]
          |	}
          |}
        """.stripMargin

      val query = validateJsonInput[PatientSearch](json)
      val fut = client.get[PatientSearch, PatientSearch](query)
      val resp = Await.result(fut, timeout)
      resp.isSuccess must beTrue
      resp.asOpt.map { searchResult =>
        searchResult.Patient must beNone
      }.get
    }

    "find someone" in {
      val json =
        """
          |{
          |	"Meta": {
          |		"DataModel": "PatientSearch",
          |		"EventType": "Query",
          |		"EventDateTime": "2017-03-14T19:35:06.047Z",
          |		"Test": true,
          |		"Destinations": [
          |			{
          |				"ID": "0f4bd1d1-451d-4351-8cfd-b767d1b488d6",
          |				"Name": "Patient Search Endpoint"
          |			}
          |		]
          |	},
          |	"Patient": {
          |		"Demographics": {
          |			"FirstName": "Timothy",
          |			"MiddleName": "Paul",
          |			"LastName": "Bixby",
          |			"DOB": "2008-01-06",
          |			"Sex": "Male"
          |		},
          |		"Notes": []
          |	}
          |}
        """.stripMargin

      val query = validateJsonInput[PatientSearch](json)
      val fut = client.get[PatientSearch, PatientSearch](query)
      val resp = Await.result(fut, timeout)
      val maybe = handleResponse(fut)
      maybe must beSome
      maybe.map { searchResult =>
        searchResult.Patient must beSome
        searchResult.Patient.get.Identifiers must not be empty
        searchResult.Patient.get.Demographics must beSome
      }.get
    }
  }
}
