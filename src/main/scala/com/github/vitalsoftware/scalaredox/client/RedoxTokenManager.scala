package com.github.vitalsoftware.scalaredox.client

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Path./
import akka.stream.Materializer
import org.joda.time.DateTime
import play.api.libs.json.Json

import scala.collection.concurrent.TrieMap
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

/**
 * Redox uses a perishable accessToken with an expiry to authenticate requests. The accessToken is obtained by
 * exchanging a apiKey and an apiSecret. On the first exchange we receive:
 *  1. accessToken
 *  2. expiry time
 *  3. refreshToken
 *
 *  We use the RefreshToken obtained from the first request to obtain new accessTokens as they expire.
 *
 *  The RedoxTokenManager schedules the refresh and storage of accessTokens and keeps them valid,
 *  so a client doesn't have to authenticate each time it makes a request.
 *
 *  @param client com.github.vitalsoftware.scalaredox.client.HttpClient instance
 *  @param baseRestUri the base url for Redox REST endpoint.
 */
class RedoxTokenManager(
  client: HttpClient,
  baseRestUri: Uri
)(
  implicit
  actorSystem: ActorSystem,
  materializer: Materializer
) extends RedoxClientComponents(client, baseRestUri, identity) {
  private val RefreshBuffer = 1.minute

  /**
   * Maps an apiKey to fetched AuthInfo.
   */
  private val TokenStore: TrieMap[String, AuthInfo] = TrieMap.empty

  /**
   * Returns an access token for given credentials. If the access token doesn't exist,
   * authorize with Redox to obtain a new access token.
   */
  def getAccessToken(apiKey: String, apiSecret: String): Future[AuthInfo] =
    TokenStore
      .get(apiKey)
      .map(Future.successful(_))
      .getOrElse {
        val authInfo = authenticate(apiKey, apiSecret)
        authInfo.foreach(setAuthAndScheduleRefresh(apiKey, _))
        authInfo
      }

  /** Authorize to Redox, returning a Future containing the access and refresh tokens. */
  protected def authenticate(apiKey: String, apiSecret: String): Future[AuthInfo] = {
    val req = baseRequest(baseRestUri.withPath(/("auth") / "authenticate").toString())
      .withMethod("POST")
      .withBody(Json.toJson(AuthRequest(apiKey = apiKey, secret = apiSecret)))
    execute[AuthInfo](req).map { result =>
      parseAuthResponse(result, "Cannot authenticate. Check configuration 'redox.apiKey' & 'redox.secret'")
    }
  }

  /** Set a thread-safe auth-token, and schedule a refresh. */
  private def setAuthAndScheduleRefresh(apiKey: String, authInfo: AuthInfo): Unit = {
    TokenStore.update(apiKey, authInfo)
    scheduleRefresh(apiKey: String, authInfo)
  }

  /**
   * Refresh the auth token a minute before it expires. Set and schedule a new refresh to occur.
   * NOTE: If this method is overridden, scheduling and storing the new auth token will
   * not be available to the implementing class.
   */
  protected def scheduleRefresh(apiKey: String, auth: AuthInfo): Unit = {
    val delay = auth.expires.getMillis - DateTime.now.getMillis - RefreshBuffer.toMillis
    actorSystem.scheduler.scheduleOnce(delay.millis) {
      refresh(apiKey, auth).foreach(setAuthAndScheduleRefresh(apiKey, _))
    }
  }

  /** Refresh the access and refresh tokens. */
  protected def refresh(apiKey: String, auth: AuthInfo): Future[AuthInfo] = {
    val req = baseRequest(baseRestUri.withPath(/("auth") / "refreshToken").toString())
      .withMethod("POST")
      .withBody(Json.toJson(RefreshRequest(apiKey = apiKey, refreshToken = auth.refreshToken)))
    execute[AuthInfo](req).map(result => parseAuthResponse(result, "Cannot refresh OAuth2 token"))
  }

  /** Parse the result of an auth request, either returning a new AuthInfo object, or throwing an error. */
  private def parseAuthResponse(authResult: RedoxResponse[AuthInfo], errMsg: String) =
    authResult.result.fold(
      error => throw RedoxAuthorizationException(s"$errMsg: ${error.Errors.map(_.Text).mkString(",")}"),
      identity
    )
}
