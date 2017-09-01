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

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.exceptions._
import com.auth0.jwt.interfaces.DecodedJWT
import play.api.mvc.{ Headers, RequestHeader, Result, Results }

import scala.util.Try
import scala.util.matching.Regex

case class TokenFilter( verifier: JWTVerifier, realm: String, altVerifiers: JWTVerifier* ) {

  final def filter( rh: RequestHeader ): Either[Result, DecodedJWT] = {
    extractToken( rh.headers ) match {
      case Some( token ) =>
        val t0 = Try { verifier.verify( token ) }
        val t = altVerifiers.foldLeft( t0 ) { ( t, v ) => t.orElse( Try { v.verify( token ) } ) }
        t.map( Right.apply ).recover {
          case e: JWTDecodeException             => Left( makeUnauthorizedResponse( Some( "invalid_token" ), Some( "Token is not a JWT" ) ) )
          case e: AlgorithmMismatchException     => Left( makeUnauthorizedResponse( Some( "invalid_token" ), Some( "Algorithm mismatch" ) ) )
          case e: SignatureVerificationException => Left( makeUnauthorizedResponse( Some( "invalid_token" ), Some( "Token signature invalid" ) ) )
          case e: TokenExpiredException          => Left( makeUnauthorizedResponse( Some( "invalid_token" ), Some( "Token expired" ) ) )
          case e: InvalidClaimException          => Left( makeUnauthorizedResponse( Some( "invalid_token" ), Some( "Claims do not match verifier" ) ) )
        }.get
      case None => Left( makeUnauthorizedResponse() )
    }
  }

  final def extractToken( headers: Headers ): Option[String] = {
    headers.get( "Authorization" ) match {
      case Some( header ) => header match {
        case tokenRegexp( token ) => Some( token )
        case _                    => None
      }
      case None => None
    }
  }

  protected lazy val tokenRegexp: Regex = "(?i)Bearer (.*)".r.anchored

  protected def makeUnauthorizedResponse( error: Option[String] = None, errorDescription: Option[String] = None ): Result = {
    val errorMsg = error.map( e => s""", error="$e"""" ).getOrElse( "" )
    val errorDescriptionMsg = errorDescription.map( e => s""", error_description="$e"""" ).getOrElse( "" )
    val challenge = s"""Bearer realm="$realm"$errorMsg$errorDescriptionMsg"""
    Results.Unauthorized.withHeaders( ( "WWW-Authenticate", challenge ) )
  }

}
