package ch.datascience.graph.init.client

import play.api.libs.ws.{ WSAuthScheme, WSClient }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TokenFetchClient(
    val clientId:     String,
    val clientSecret: String,
    val providerUrl:  String,
    ws:               WSClient
) {

  def getAccessToken: Future[String] = {
    for {
      response <- ws.url( providerUrl ).withAuth( clientId, clientSecret, WSAuthScheme.BASIC ).post( Map( "grant_type" -> Seq( "client_credentials" ) ) )
    } yield response.status match {
      case 200 => ( response.json \ "access_token" ).as[String]
      case _   => throw new RuntimeException( response.statusText )
    }
  }

}
