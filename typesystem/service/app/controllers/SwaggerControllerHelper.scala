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

package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsObject
import play.api.mvc._

import scala.concurrent.Future

trait SwaggerControllerHelper { this: Controller =>

  def swaggerSpec: JsObject

  def getSwagger: Action[Unit] = Action.async( BodyParsers.parse.empty ) { implicit request =>
    Future {
      val host = extractHost( request )
      val scheme = extractScheme( request )

      val content = swaggerSpec.transform( SwaggerMappers.updateHostAndSchemes( host, List( scheme ) ) ).get
      Ok( content )
    }
  }

  protected def extractHost[A]( request: Request[A] ): String = {
    request.headers.get( "X-Forwarded-Host" ).getOrElse(
      request.host
    )
  }

  protected def extractScheme[A]( request: Request[A] ): String = {
    request.headers.get( "X-Forwarded-Proto" ).getOrElse(
      if ( request.secure )
        "https"
      else
        "http"
    )
  }

}
