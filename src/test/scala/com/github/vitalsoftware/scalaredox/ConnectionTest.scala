package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.{AuthInfo, RedoxClient}
import com.github.vitalsoftware.scalaredox.models._
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions
import play.api.libs.json.{JsError, Json}

import concurrent.{Await, Future}
import scala.concurrent.duration._

class ConnectionTest extends Specification with NoTimeConversions {

  val conf = ConfigFactory.load("resources/reference.conf")
  val client = new RedoxClient(conf)


  protected def validateAuth(auth: AuthInfo): Boolean = {
    auth.accessToken must not be empty
    auth.refreshToken must not be empty
    auth.expires.compareTo(org.joda.time.DateTime.now) must be_>(0)
  }
  
  "authorize" should {

    "return an auth token" in {
      val fut = client.authorize()
      val auth = Await.result(fut, 2.seconds)
      validateAuth(auth)
    }
  }

  "refresh token" should {

    "obtain then refresh an auth token" in {
      val f1 = client.authorize()
      val auth1 = Await.result(f1, 2.seconds)
      val f2 = client.refresh(auth1)
      val auth2 = Await.result(f2, 2.seconds)
      validateAuth(auth2)
    }
  }

  "query ClinicalSummary" should {

    "return an error" in {
      val shouldFailQuery = ClinicalSummaryQuery(
        Meta(DataModel = "", EventType = ""),
        Patient(Demographics = Some(Demographics("John", "Doe", DateTime.parse("1970-1-1"), Sex = Gender.Male)))
      )
      val fut = client.getClinicalSummary(shouldFailQuery)
      val resp = Await.result(fut, 5.seconds)
      resp.isError must beTrue
      resp.get must throwA[Exception]
      resp.getError.Errors must not be empty
    }

    "return an object" in {
      val json =
        """
          |{
          |	"Meta": {
          |		"DataModel": "Clinical Summary",
          |		"EventType": "Query",
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

      Json.parse(json).validate[ClinicalSummaryQuery].fold(
        invalid = err => throw new RuntimeException(JsError.toJson(JsError(err)).toString()),
        valid = identity
      )
      val query = Json.fromJson[ClinicalSummaryQuery](Json.parse(json)).get
      val fut = client.getClinicalSummary(query)
      val resp = Await.result(fut, 5.seconds)
      if (resp.isError) throw new RuntimeException(resp.getError.Errors.map(_.Text).mkString(","))
      resp.isSuccess must beTrue
      resp.getError must throwA[Exception]
      resp.asOpt.map { clinicalSummary =>

        // Meta
        val meta = clinicalSummary.Meta
        meta.EventType.toLowerCase must startWith("query")
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
        meds.head.Rate must beSome
        meds.head.Route must beSome
        meds.head.Frequency must beSome

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
  }
}
