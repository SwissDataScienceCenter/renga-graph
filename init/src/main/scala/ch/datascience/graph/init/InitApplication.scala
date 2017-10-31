/*
 * Copyright 2017 - Swiss Data Science Center (SDSC)
 * A partnership between École Polytechnique Fédérale de Lausanne (EPFL) and
 * Eidgenössische Technische Hochschule Zürich (ETHZ).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.datascience.graph.init

import java.io.{ FileNotFoundException, InputStream }

import akka.actor.ActorSystem
import ch.datascience.graph.init.client._
import ch.datascience.graph.naming.NamespaceAndName
import ch.datascience.graph.types.persistence.model
import com.typesafe.config.{ Config, ConfigFactory }
import org.slf4j.{ Logger, LoggerFactory }
import play.api.libs.json.{ JsError, JsSuccess, JsValue, Json }
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ Future, Promise }
import scala.util.{ Failure, Success, Try }

/**
 * Created by johann on 21/06/17.
 */
object InitApplication {

  lazy val logger: Logger = LoggerFactory.getLogger( "application.InitApplication" )

  def main( args: Array[String] ): Unit = {
    app()
  }

  def app(): Unit = {
    val config: Config = ConfigFactory.load()
    logger.info( s"Started with configuration: $config" )

    val script: String = config.getString( "application.script" )
    val typeInitJson: JsValue = Json.parse( readResource( s"/$script" ) )
    val typeInit = typeInitJson.validate[TypeInit]( TypeInitFormat ) match {
      case JsSuccess( ti, _ ) =>
        logger.info( s"Successfully read type init" )
        ti
      case e: JsError =>
        logger.error( s"Cannot parse type init" )
        throw new RuntimeException( Json.prettyPrint( JsError.toJson( e ) ) )
    }

    val client: WSClient = WSClientFactory.client
    val endPromise: Promise[Unit] = Promise()
    endPromise.future.onComplete { _ => client.close(); ActorSystem().terminate(); sys.exit() }

    var currentFuture: Future[Any] = null
    val tokenPromise: Promise[String] = Promise()

    currentFuture = waitForApi( client, config )
    currentFuture.onComplete {
      case Success( _ )      => logger.info( s"API is up" )
      case Failure( reason ) => logger.error( reason.getClass.getCanonicalName )
    }

    currentFuture = currentFuture.flatMap { _ => waitForTokenService( client, config ) }
    currentFuture.onComplete {
      case Success( _ )      => logger.info( s"Token service is up" )
      case Failure( reason ) => logger.error( reason.getClass.getCanonicalName )
    }

    currentFuture = currentFuture.flatMap { _ => fetchToken( client, config ) }.map { token => tokenPromise.success( token ); token }
    currentFuture.onComplete {
      case Success( res )    => logger.info( s"$res" )
      case Failure( reason ) => logger.error( reason.getClass.getCanonicalName )
    }

    currentFuture = currentFuture.flatMap { _ => initSystemPropertyKeys( client, getToken( tokenPromise ), config, typeInit ) }
    currentFuture.onComplete {
      case Success( res )    => logger.info( s"$res" )
      case Failure( reason ) => logger.error( reason.getClass.getCanonicalName )
    }

    currentFuture = currentFuture.flatMap( _ => initGraphDomain( client, getToken( tokenPromise ), config, typeInit ) )
    currentFuture.onComplete {
      case Success( res )    => logger.info( s"$res" )
      case Failure( reason ) => logger.error( reason.getClass.getCanonicalName )
    }

    currentFuture = currentFuture.flatMap( _ => initPropertyKeys( client, getToken( tokenPromise ), config, typeInit ) )
    currentFuture.onComplete {
      case Success( res )    => logger.info( s"$res" )
      case Failure( reason ) => logger.error( reason.getClass.getCanonicalName )
    }

    currentFuture = currentFuture.flatMap( _ => initEdgeLabels( client, getToken( tokenPromise ), config, typeInit ) )
    currentFuture.onComplete {
      case Success( res )    => logger.info( s"$res" )
      case Failure( reason ) => logger.error( reason.getClass.getCanonicalName )
    }

    currentFuture = currentFuture.flatMap( _ => initNamedTypes( client, getToken( tokenPromise ), config, typeInit ) )
    currentFuture.onComplete {
      case Success( res )    => logger.info( s"$res" )
      case Failure( reason ) => logger.error( reason.getClass.getCanonicalName )
    }

    currentFuture.onComplete { _ => endPromise.success( () ) }

  }

  def readResource( resource: String ): InputStream = {
    Try( getClass.getResourceAsStream( resource ) )
      .recover { case _ => throw new FileNotFoundException( resource ) }
      .get
  }

  def waitForApi( client: WSClient, config: Config ): Future[Unit] = {
    val url = s"${config.getString( "graph.api.types" )}/scope/type"

    def check(): Future[Unit] = {
      logger.info( s"Checking: $url" )
      val f = client.url( url ).withRequestTimeout( 30.seconds ).get()
      f.onComplete { res => logger.info( s"Got response: $res" ) }
      for {
        response <- f
      } yield response.status match {
        case 200 => ()
        case _ =>
          logger.error( s"Got unexpected response: $response" )
          throw new RuntimeException( response.statusText )
      }
    }

    def checkN( n: Int ): Future[Unit] = {
      require( n > 0 )
      logger.info( s"$n tries remaining" )
      n match {
        case 1 => check()
        case _ => check().recoverWith {
          case _ =>
            scala.concurrent.blocking { Thread.sleep( 10000 ) }
            checkN( n - 1 )
        }
      }
    }

    checkN( 10 )
  }

  def waitForTokenService( client: WSClient, config: Config ): Future[Unit] = {
    val url = config.getString( "authorization.provider.url" )

    def check(): Future[Unit] = {
      logger.info( s"Checking: $url" )
      val f = client.url( url ).withRequestTimeout( 30.seconds ).get()
      f.onComplete { res => logger.info( s"Got response: $res" ) }
      for {
        response <- f
      } yield response.status match {
        case 405 => ()
        case _ =>
          logger.error( s"Got unexpected response: $response" )
          throw new RuntimeException( response.statusText )
      }
    }

    def checkN( n: Int ): Future[Unit] = {
      require( n > 0 )
      logger.info( s"$n tries remaining" )
      n match {
        case 1 => check()
        case _ => check().recoverWith {
          case _ =>
            scala.concurrent.blocking { Thread.sleep( 10000 ) }
            checkN( n - 1 )
        }
      }
    }

    checkN( 10 )
  }

  def fetchToken( client: WSClient, config: Config ): Future[String] = {
    val tfc = new TokenFetchClient( config.getString( "authorization.client_id" ), config.getString( "authorization.client_secret" ), config.getString( "authorization.provider.url" ), client )
    tfc.getAccessToken
  }

  def getToken( tokenPromise: Promise[String] ): String = {
    tokenPromise.future.value.get.get
  }

  def initSystemPropertyKeys( client: WSClient, accessToken: String, config: Config, typeInit: TypeInit ): Future[Seq[model.SystemPropertyKey]] = {
    val spkc = new SystemPropertyKeyClient( config.getString( "graph.api.types" ), accessToken, client )
    Future.traverse( typeInit.systemPropertyKeys ) { pk => spkc.getOrCreateSystemPropertyKey( pk.name, pk.dataType, pk.cardinality ) }
  }

  def initGraphDomain( client: WSClient, accessToken: String, config: Config, typeInit: TypeInit ): Future[Seq[model.GraphDomain]] = {
    val gdc = new GraphDomainClient( config.getString( "graph.api.types" ), accessToken, client )
    Future.traverse( typeInit.graphDomains ) { gd => gdc.getOrCreateGraphDomain( gd ) }
  }

  def initPropertyKeys( client: WSClient, accessToken: String, config: Config, typeInit: TypeInit ): Future[Seq[model.RichPropertyKey]] = {
    val pkc = new PropertyKeyClient( config.getString( "graph.api.types" ), accessToken, client )
    Future.traverse( typeInit.propertyKeys ) { pk =>
      val NamespaceAndName( namespace, name ) = pk.key
      pkc.getOrCreatePropertyKey( namespace, name, pk.dataType, pk.cardinality )
    }
  }

  def initEdgeLabels( client: WSClient, accessToken: String, config: Config, typeInit: TypeInit ): Future[Seq[model.RichEdgeLabel]] = {
    val elc = new EdgeLabelClient( config.getString( "graph.api.types" ), accessToken, client )
    Future.traverse( typeInit.edgeLabels ) { el =>
      val NamespaceAndName( namespace, name ) = el.key
      elc.getOrCreateEdgeLabel( namespace, name, el.multiplicity )
    }
  }

  def initNamedTypes( client: WSClient, accessToken: String, config: Config, typeInit: TypeInit ): Future[Seq[model.RichNamedType]] = {
    val ntc = new NamedTypeClient( config.getString( "graph.api.types" ), accessToken, client )
    val promiseMap = ( for {
      namedType <- typeInit.namedTypes
    } yield namedType.typeId -> Promise[model.RichNamedType]() ).toMap

    Future.traverse( typeInit.namedTypes ) { nt =>
      val NamespaceAndName( namespace, name ) = nt.typeId
      val superTypesDone = Future.traverse( nt.superTypes ) { st => promiseMap( st ).future }
      val res = superTypesDone.flatMap { _ => ntc.getOrCreateNamedType( namespace, name, nt.superTypes.toSeq, nt.properties.toSeq ) }
      res.onComplete { result => promiseMap( nt.typeId ).complete( result ) }
      res
    }
  }

}
