package com.github.vitalsoftware.scalaredox.receiver

import com.github.vitalsoftware.scalaredox.receiver.ReceiveController._
import com.github.vitalsoftware.util.JsonNaming.KebabCase
import play.api.libs.json._
import play.api.mvc._
import play.api.Logger
import play.api.libs.json.JsonConfiguration.Aux

import scala.concurrent.Future

trait ReceiveController extends BaseController {
  def verificationToken: String
  val logger: Logger

  /**
   * Validate an initial challenge from Redox to authenticate our server as a
   * destination, or validate a normal Redox event request and delegate to the
   * verifiedAction
   *
   * Redox documentation says:
   * > Verification POSTs will include a challenge value and your destination’s
   * > verification token (that you specified when you set up the destination
   * > record) in the body of the POST. Non-verification POSTs from Redox will
   * > always include the verification token in the header of the message.
   *
   * @see http://developer.redoxengine.com/getting-started/create-a-destination/
   */
  protected def validatedAction(verifiedAction: RedoxRequest => Future[Result]): Action[JsValue] =
    Action(parse.json).async({ request: Request[JsValue] =>
      RedoxRequest(request).token match {
        case Some(token) if token == verificationToken =>
          verifiedAction(RedoxRequest(request))
        case Some(token) =>
          // The verification token is incorrect
          logger.error(s"Redox webhook had an incorrect token from ${RedoxRequest(request)}")
          Future.successful(Forbidden(s"Validation failed."))
        case None =>
          challengeResponse(RedoxRequest(request))
      }
    })

  private def challengeResponse(request: RedoxRequest): Future[Result] = request.underlying.body.validate[Challenge].fold(
    (errors: Seq[(JsPath, Seq[JsonValidationError])]) => {
      // The challenge has an invalid format
      logger.error(s"Redox webhook challenge had errors (${JsError.toJson(errors)}) from $request")
      logger.debug(s"Failed challenge body was: ${request.underlying.body.toString()}")
      Future.successful(Forbidden(s"Challenge failed."))
    },
    (challenge: Challenge) => {
      if (challenge.verificationToken == verificationToken) {
        // The challenge is successful: we must respond with the challenge token (only)
        logger.info(s"Redox endpoint initialized via challenge from $request")
        Future.successful(Ok(challenge.challenge))
      } else {
        // The challenge has an invalid validation token
        logger.error(s"Redox challenge had invalid token from $request")
        Future.successful(Forbidden(s"Challenge failed."))
      }
    }
  )
}

object ReceiveController {
  case class Challenge(verificationToken: String, challenge: String)

  implicit val config: Aux[Json.MacroOptions] = JsonConfiguration(KebabCase)
  implicit val challengeFormat: Format[Challenge] = Json.format[Challenge]
}
