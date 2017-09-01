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
