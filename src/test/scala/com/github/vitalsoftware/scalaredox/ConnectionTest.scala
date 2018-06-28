package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.AuthInfo
import com.github.vitalsoftware.scalaredox.models._
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import play.api.libs.json.{ JsError, Json }

import concurrent.{ Await, Future }
import scala.concurrent.duration._

class ConnectionTest extends Specification with RedoxTest {

  protected def validateAuth(auth: AuthInfo): Boolean = {
    auth.accessToken must not be empty
    auth.refreshToken must not be empty
    auth.expires.compareTo(org.joda.time.DateTime.now) must be_>(0)
  }

  "authorize" should {

    "return an auth token" in {
      val fut = client.authorize()
      val auth = Await.result(fut, timeout)
      validateAuth(auth)
    }
  }

  "refresh token" should {

    "obtain then refresh an auth token" in {
      val f1 = client.authorize()
      val auth1 = Await.result(f1, timeout)
      val f2 = client.refresh(auth1)
      val auth2 = Await.result(f2, timeout)
      validateAuth(auth2)
    }
  }
}
