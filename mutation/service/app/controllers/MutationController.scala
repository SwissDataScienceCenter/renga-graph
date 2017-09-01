package controllers

import java.util.UUID
import javax.inject.{ Inject, Singleton }

import authorization.JWTVerifierProvider
import ch.datascience.graph.elements.mutation.Mutation
import ch.datascience.graph.elements.mutation.json.MutationFormat
import ch.datascience.graph.elements.mutation.log.model.EventStatus
import ch.datascience.graph.elements.mutation.log.model.json._
import ch.datascience.graph.service.security.TokenFilterAction
import com.auth0.jwt.JWTVerifier
import models.{ RequestDAO, RequestWorker, ResponseWorker }
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

/**
 * Created by johann on 07/06/17.
 */
@Singleton
class MutationController @Inject() (
    verifierProvider:             JWTVerifierProvider,
    protected val requestWorker:  RequestWorker,
    protected val responseWorker: ResponseWorker,
    protected val dao:            RequestDAO
) extends Controller
  with JsonComponent {

  implicit lazy val ec: ExecutionContext = play.api.libs.concurrent.Execution.defaultContext

  implicit lazy val mutationFormat: Format[Mutation] = MutationFormat

  lazy val verifier: JWTVerifier = verifierProvider.get

  def postMutation: Action[Mutation] = TokenFilterAction( verifier ).async( bodyParseJson[Mutation]( MutationFormat ) ) { implicit request =>
    val query = request.body
    //TODO: get token from header
    val token: JsValue = Json.parse( """{ "my-token-is-empty": true }""" )
    val event = JsObject( Seq(
      "query" -> Json.toJson( query ),
      "token" -> token
    ) )

    for {
      event <- requestWorker.add( event )
    } yield {
      Ok( Json.toJson( event ) )
    }
  }

  def findById( id: String ): Action[Unit] = TokenFilterAction( verifier ).async( BodyParsers.parse.empty ) { implicit request =>
    val json = JsString( id )
    json.validate[UUID] match {
      case JsError( e ) => Future.successful( BadRequest( JsError.toJson( e ) ) )
      case JsSuccess( uuid, _ ) =>
        val future = dao.findByIdWithResponse( uuid )
        future map {
          case Some( ( req, optRes ) ) => Ok( Json.toJson( EventStatus( req, optRes ) ) )
          case None                    => NotFound
        }
    }
  }

}
