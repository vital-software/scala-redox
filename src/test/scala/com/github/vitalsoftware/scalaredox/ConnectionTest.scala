package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.RedoxClient
import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification

import concurrent.{Await, Future}
import scala.concurrent.duration._

class ConnectionTest extends Specification {

  val conf = ConfigFactory.load("resources/reference.conf")
  val client = new RedoxClient(conf)

  "authorize" should {

    "return a token" in {
      val fut = client.authorize()
      val auth = Await.result(fut, 20000.milli)
      auth.accessToken must not be empty
      auth.refreshToken must not be empty
      auth.expires.compareTo(org.joda.time.DateTime.now) must be_>(0)
    }
  }

  "refresh token" should {

    "obtain a token" in {
      true
    }

    "obtain a new refresh token" in {
      true
    }
  }

  "query ClinicalSummary" should {

    "return an error" in {
      true
    }

    "return an object" in {
      true
    }
  }
}
