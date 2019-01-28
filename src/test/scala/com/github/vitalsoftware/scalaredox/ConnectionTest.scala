package com.github.vitalsoftware.scalaredox

import com.github.vitalsoftware.scalaredox.client.{ AuthInfo, RedoxTokenManager }
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import play.api.routing.sird._
import play.api.test.WsTestClient
import play.core.server.Server

import concurrent.Await
import scala.concurrent.duration._

class ConnectionTest extends Specification with RedoxTest {

  protected def validateAuth(auth: AuthInfo): Boolean = {
    auth.accessToken must not be empty
    auth.refreshToken must not be empty
    auth.expires.compareTo(org.joda.time.DateTime.now) must be_>(0)
  }

  "authorize" should {

    "return an auth token" in {
      val fut = tokenManager.getAccessToken(conf.apiKey, conf.apiSecret)
      val auth = Await.result(fut, timeout)
      validateAuth(auth)
    }
  }

  "refresh token" should {

    "obtain then refresh an auth token" in {
      // create a fake server response
      Server.withRouterFromComponents() { components =>
        import play.api.mvc.Results._
        import components.{ defaultActionBuilder => Action }
      {
        case POST(p"/auth/authenticate") => Action {
          Ok(Json.obj(
            "accessToken" -> "access",
            // expires in 1 minute 4 seconds
            // should trigger an refresh in 4 seconds as the RefreshBuffer is 1 minute
            "expires" -> new DateTime(DateTime.now().getMillis + 1.minute.toMillis + 4.seconds.toMillis).toString,
            "refreshToken" -> "4ed7b234-9bde-4a9c-9c86-e1bc6e535321",
          ))
        }
        case POST(p"/auth/refreshToken") => Action {
          Ok(Json.obj(
            "accessToken" -> "refreshed",
            // let time for test to pass
            "expires" -> new DateTime(DateTime.now().getMillis + 1.minute.toMillis + 1.minute.toMillis).toString,
            "refreshToken" -> "4ed7b234-9bde-4a9c-9c86-e1bc6e535321",
          ))
        }
      }
      } { implicit port =>
        WsTestClient.withClient { httpClient =>
          val tokenManager = new RedoxTokenManager(httpClient, "/auth")

          val f1 = tokenManager.getAccessToken(conf.apiKey, conf.apiSecret)
          val auth1 = Await.result(f1, timeout)
          auth1.accessToken mustEqual ("access")

          val f2 = tokenManager.getAccessToken(conf.apiKey, conf.apiSecret)
          val auth2 = Await.result(f2, timeout)
          auth2.accessToken mustEqual ("access")

          // wait 5 seconds
          Thread.sleep(5.seconds.toMillis)

          val f3 = tokenManager.getAccessToken(conf.apiKey, conf.apiSecret)
          val auth3 = Await.result(f3, timeout)
          auth3.accessToken mustEqual ("refreshed")
        }
      }
    }
  }
}
