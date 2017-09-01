package controllers.management

import java.sql.SQLException
import java.util.UUID
import javax.inject.Inject

import authorization.JWTVerifierProvider
import ch.datascience.graph.service.security.TokenFilterAction
import ch.datascience.graph.types.persistence.model.json._
import ch.datascience.graph.types.{ Cardinality, DataType }
import com.auth0.jwt.JWTVerifier
import controllers.JsonComponent
import injected.OrchestrationLayer
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._

/**
 * Created by johann on 26/04/17.
 */
class SystemPropertyKeyController @Inject() (
    verifierProvider:           JWTVerifierProvider,
    protected val orchestrator: OrchestrationLayer
) extends Controller with JsonComponent {

  lazy val verifier: JWTVerifier = verifierProvider.get

  def index: Action[Unit] = TokenFilterAction( verifier ).async( BodyParsers.parse.empty ) { implicit request =>
    val all = orchestrator.systemPropertyKeys.all()
    all.map( seq => Json.toJson( seq ) ).map( json => Ok( json ) )
  }

  def findByIdOrName( idOrName: String ): Action[Unit] = TokenFilterAction( verifier ).async( BodyParsers.parse.empty ) { implicit request =>
    val json = JsString( idOrName )
    val future = json.validate[UUID].asOpt match {
      case Some( id ) => orchestrator.systemPropertyKeys.findById( id )
      case None       => orchestrator.systemPropertyKeys.findByName( idOrName )
    }
    future map {
      case Some( propertyKey ) => Ok( Json.toJson( propertyKey ) )
      case None                => NotFound
    }
  }

  def create: Action[( String, DataType, Cardinality )] = TokenFilterAction( verifier ).async( bodyParseJson[( String, DataType, Cardinality )]( SystemPropertyKeyRequestFormat ) ) { implicit request =>
    val ( name, dataType, cardinality ) = request.body
    val future = orchestrator.systemPropertyKeys.createSystemPropertyKey( name, dataType, cardinality )
    future map { propertyKey => Ok( Json.toJson( propertyKey ) ) } recover {
      case e: SQLException =>
        //TODO: log exception
        Conflict // Avoids send of 500 INTERNAL ERROR if duplicate creation
    }
  }

}
