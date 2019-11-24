package com.github.vitalsoftware.helpers

import com.github.vitalsoftware.helpers.WithReceiveController.ReceiveController
import play.api.Application
import play.api.mvc.{ Request, Result }
import play.api.test.Injecting
import com.github.vitalsoftware.scalaredox.receiver.{ RedoxRequest, ReceiveController => BaseReceiveController }
import javax.inject.Inject
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.mvc.{ Action, ControllerComponents }

import scala.concurrent.Future

/**
 * Trait for tests and context objects involving the receive controller
 */
trait WithReceiveController extends Injecting {
  val app: Application

  protected lazy val controller: ReceiveController = inject[ReceiveController]

  def receive(req: Request[JsValue]): Future[Result] = controller.receive().apply(req)
}

object WithReceiveController {
  final val ValidToken = "foo"
  final val InvalidToken = "bar"

  class ReceiveController @Inject() (val controllerComponents: ControllerComponents) extends BaseReceiveController {
    override val logger: Logger = play.api.Logger(this.getClass)
    override val verificationToken: String = WithReceiveController.ValidToken

    def receive: Action[JsValue] = validatedAction { request: RedoxRequest =>
      Future.successful(Ok("Done"))
    }
  }
}
