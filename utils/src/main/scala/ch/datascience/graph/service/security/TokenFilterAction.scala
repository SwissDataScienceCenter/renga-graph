package ch.datascience.graph.service.security

import com.auth0.jwt.{ JWT, JWTVerifier }
import play.api.mvc._

import scala.concurrent.Future

/**
 * Created by johann on 13/07/17.
 */
case class TokenFilterAction( verifier: JWTVerifier, realm: String, altVerifiers: JWTVerifier* )
  extends ActionBuilder[RequestWithToken]
  with ActionTransformer[Request, RequestWithToken]
  with AbstractFilterBeforeBodyParseAction {

  lazy val tokenFilter: TokenFilter = TokenFilter( verifier, realm, altVerifiers: _* )

  protected def transform[A]( request: Request[A] ): Future[RequestWithToken[A]] = Future.successful {
    require( request.tags.get( "VERIFIED_TOKEN" ).contains( tokenFilter.extractToken( request.headers ).get ) )
    val token = JWT.decode( tokenFilter.extractToken( request.headers ).get )
    new RequestWithToken[A]( token, request )
  }

  def filter( rh: RequestHeader ): Either[Result, RequestHeader] = {
    tokenFilter.filter( rh ).right.map( token => rh.withTag( "VERIFIED_TOKEN", token.getToken ) )
  }

}

object TokenFilterAction {

  def apply( verifier: JWTVerifier, altVerifiers: JWTVerifier* ): TokenFilterAction = TokenFilterAction( verifier, realm = "", altVerifiers: _* )

}
