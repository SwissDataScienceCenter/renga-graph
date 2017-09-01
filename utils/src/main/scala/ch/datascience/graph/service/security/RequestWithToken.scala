package ch.datascience.graph.service.security

import com.auth0.jwt.interfaces.DecodedJWT
import play.api.mvc.{ Request, WrappedRequest }

/**
 * Created by johann on 13/07/17.
 */
class RequestWithToken[+A]( val token: DecodedJWT, request: Request[A] ) extends WrappedRequest[A]( request )
