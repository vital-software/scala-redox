package com.github.vitalsoftware.scalaredox.client

import java.io.Closeable

import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.{ StandaloneWSRequest, WSClient }

/**
 * Designed to unify interfaces of play.api.libs.ws.StandaloneWSClient and play.api.libs.ws.WSClient
 * so that these can be used interchangeably. Can be used to accommodate play's internal WsClient or
 * StandaloneAhcWSClient bundled with the library.
 */
trait HttpClient extends Closeable {
  def url(url: String): StandaloneWSRequest
}

object HttpClient {
  implicit def fromWsClient(wsClient: WSClient) = new HttpClient {
    def url(url: String): StandaloneWSRequest = wsClient.url(url)

    /**
     * Calling this is probably risky as wsClient might get created from DI container and injected.
     * So this basically does nothing. If WsClient is used, it is expected that the caller handles releasing resources.
     */
    override def close(): Unit = wsClient.close()
  }

  implicit def fromStandaloneAhcWSClient(standaloneAhcWSClient: StandaloneAhcWSClient) = new HttpClient {
    override def url(url: String): StandaloneWSRequest = standaloneAhcWSClient.url(url)

    override def close(): Unit = standaloneAhcWSClient.close()
  }
}
