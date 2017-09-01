package ch.datascience.graph.service.security

import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

import com.typesafe.config.Config
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.{ ExecutionContext, Future }

/**
 * Created by johann on 14/07/17.
 */
object PublicKeyReader {

  def getRSAPublicKey( config: Configuration )( implicit ws: WSClient, ec: ExecutionContext ): Future[RSAPublicKey] = {
    config.getString( "public-key" ) match {
      case Some( encodedKey ) => Future.successful( readRSAPublicKey( encodedKey ) )
      case None =>
        val strict = config.getBoolean( "strict" ).getOrElse( true )
        val provider = config.getConfig( "public-key-provider" )
        if ( strict )
          Future.failed( new IllegalArgumentException( "Bad config: strict mode but no key given" ) )
        else if ( provider.nonEmpty && provider.get.getString( "type" ).contains( "url" ) && provider.get.getString( "url" ).nonEmpty )
          fetchRSAPublicKey( provider.get.getString( "url" ).get )
        else
          Future.failed( new IllegalArgumentException( "Bad config: no key or provider" ) )
    }
  }

  def getRSAPublicKey( config: Config )( implicit ws: WSClient, ec: ExecutionContext ): Future[RSAPublicKey] = {
    getRSAPublicKey( Configuration( config ) )
  }

  def readRSAPublicKey( encodedKey: String ): RSAPublicKey = {
    val decoded = Base64.getDecoder.decode( encodedKey )
    val spec = new X509EncodedKeySpec( decoded )
    val factory = KeyFactory.getInstance( "RSA" )
    val key = factory.generatePublic( spec )
    key.asInstanceOf[RSAPublicKey]
  }

  def fetchRSAPublicKey( url: String )( implicit ws: WSClient, ec: ExecutionContext ): Future[RSAPublicKey] = {
    for {
      response <- ws.url( url ).get()
    } yield {
      val encodedKey = ( response.json \ "public_key" ).as[String]
      readRSAPublicKey( encodedKey )
    }
  }

}
