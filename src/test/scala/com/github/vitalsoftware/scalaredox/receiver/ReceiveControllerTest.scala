package com.github.vitalsoftware.scalaredox.receiver

import com.github.vitalsoftware.helpers.{ WithApplication, WithReceiveController }
import org.specs2.matcher.Matchers
import org.specs2.mock.Mockito
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.mvc.{ Request, Result, Results }
import play.api.test._
import play.api.{ Configuration, Environment }
import RedoxRequest.TokenHeader

import scala.concurrent.Future

/**
 * Test that actually communicates with a Kinesis endpoint (e.g. via a Kinesalite
 * container in development environments)
 */
object ReceiveControllerTest extends PlaySpecification
  with Results
  with Matchers
  with Mockito
  with WithApplication
  with WithReceiveController {

  private final val Request = FakeRequest()
    .withBody(Json.obj("foo" -> "bar"))

  private val environment = Environment.simple()

  override protected def builder: GuiceApplicationBuilder = super.builder
    .configure(Configuration.load(environment) ++ Configuration.from(Map(
      "redox-receiver.verificationToken" -> WithReceiveController.ValidToken
    )))

  // --- Tests --- //

  "ReceiveController" should {
    "provide a /receive route" in {
      def receive(req: Request[JsValue]): Future[Result] = controller.receive().apply(req)

      "that allows requests with valid tokens" in {
        "and processes normal payloads" in {
          val res: Future[Result] = receive(
            Request
              .withHeaders(TokenHeader -> WithReceiveController.ValidToken)
              .withBody(Json.obj("foo" -> "bar"))
          )

          status(res) mustEqual OK
          header("Cookie", res) must beNone
        }
      }

      "that blocks invalid requests with no token" in {
        status(receive(Request)) mustEqual FORBIDDEN
      }

      "that blocks invalid requests with bad tokens" in {
        status(receive(Request.withHeaders(
          TokenHeader -> WithReceiveController.InvalidToken
        ))) mustEqual FORBIDDEN
      }

      "that responds to valid challenge requests" in {
        val result = receive(Request.withBody(Json.obj(
          "challenge" -> "something",
          "verification-token" -> WithReceiveController.ValidToken
        )))

        status(result) mustEqual OK
        contentAsString(result) mustEqual "something"
      }

      "that blocks invalid challenge requests with no token" in {
        status(receive(Request.withBody(Json.obj(
          "challenge" -> "something",
        )))) mustEqual FORBIDDEN
      }

      "that blocks invalid challenge requests with bad tokens" in {
        status(receive(Request.withBody(Json.obj(
          "challenge" -> "something",
          "verification-token" -> WithReceiveController.InvalidToken
        )))) mustEqual FORBIDDEN
      }
    }
  }
}
