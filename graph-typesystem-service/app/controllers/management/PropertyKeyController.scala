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

package controllers.management

import java.sql.SQLException
import java.util.UUID
import javax.inject.Inject

import ch.datascience.graph.types.persistence.model.json._
import ch.datascience.graph.types.{ Cardinality, DataType }
import controllers.JsonComponent
import injected.OrchestrationLayer
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Future

/**
 * Created by johann on 26/04/17.
 */
class PropertyKeyController @Inject() ( protected val orchestrator: OrchestrationLayer ) extends Controller with JsonComponent {

  def index: Action[Unit] = Action.async( BodyParsers.parse.empty ) { implicit request =>
    val all = orchestrator.propertyKeys.all()
    all.map( seq => Json.toJson( seq ) ).map( json => Ok( json ) )
  }

  def findById( id: String ): Action[Unit] = Action.async( BodyParsers.parse.empty ) { implicit request =>
    val json = JsString( id )
    json.validate[UUID] match {
      case JsError( e ) => Future.successful( BadRequest( JsError.toJson( e ) ) )
      case JsSuccess( uuid, _ ) =>
        val future = orchestrator.propertyKeys.findById( uuid )
        future map {
          case Some( propertyKey ) => Ok( Json.toJson( propertyKey ) )
          case None                => NotFound
        }
    }
  }

  def findByNamespaceAndName( namespace: String, name: String ): Action[Unit] = Action.async( BodyParsers.parse.empty ) { implicit request =>
    val future = orchestrator.propertyKeys.findByNamespaceAndName( namespace, name )
    future map {
      case Some( propertyKey ) => Ok( Json.toJson( propertyKey ) )
      case None                => NotFound
    }
  }

  def create: Action[( String, String, DataType, Cardinality )] = Action.async( bodyParseJson[( String, String, DataType, Cardinality )]( PropertyKeyRequestFormat ) ) { implicit request =>
    val ( namespace, name, dataType, cardinality ) = request.body
    val future = orchestrator.propertyKeys.createPropertyKey( namespace, name, dataType, cardinality )
    future map { propertyKey => Ok( Json.toJson( propertyKey ) ) } recover {
      case e: SQLException =>
        //TODO: log exception
        Conflict // Avoids send of 500 INTERNAL ERROR if duplicate creation
    }
  }

}
