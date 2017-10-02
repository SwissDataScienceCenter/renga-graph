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

package ch.datascience.graph.elements.schema

import ch.datascience.graph.types.DataType
import play.api.libs.json._

object HasVertexPropertiesSchema {

  def allSchemas: Map[String, JsObject] = {
    Map(
      "HasVertexProperties" -> hasVertexPropertiesSchema,
      "HasBaseVertexProperties" -> hasBaseVertexPropertiesSchema
    )
  }

  def hasVertexPropertiesSchema: JsObject = {
    JsObject( Seq(
      "type" -> JsString( "object" ),
      "properties" -> JsObject( propertiesMap )
    ) )
  }

  def propertiesMap: Map[String, JsObject] = {
    val seq = for {
      dt <- DataType.dataTypes
    } yield s"${dt.name.toLowerCase}_properties" -> JsObject( Seq(
      "type" -> JsString( "array" ),
      "items" -> JsObject( Seq( "$ref" -> JsString( s"#/definitions/${dt.name.capitalize}VertexProperty" ) ) )
    ) )
    seq.toMap
  }

  def hasBaseVertexPropertiesSchema: JsObject = {
    JsObject( Seq(
      "type" -> JsString( "object" ),
      "properties" -> JsObject( basePropertiesMap )
    ) )
  }

  def basePropertiesMap: Map[String, JsObject] = {
    val seq = for {
      dt <- DataType.dataTypes
    } yield s"${dt.name.toLowerCase}_properties" -> JsObject( Seq(
      "type" -> JsString( "array" ),
      "items" -> JsObject( Seq( "$ref" -> JsString( s"#/definitions/${dt.name.capitalize}BaseVertexProperty" ) ) )
    ) )
    seq.toMap
  }

}
