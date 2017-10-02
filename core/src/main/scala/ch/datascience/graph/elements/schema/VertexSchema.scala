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

import play.api.libs.json._

object VertexSchema {

  def allSchemas: Map[String, JsObject] = {
    Map( "Vertex" -> vertexSchema, "NewVertex" -> newVertexSchema )
  }

  def vertexSchema: JsObject = {
    JsObject( Seq(
      "allOf" -> JsArray( Seq(
        JsObject( Seq(
          "type" -> JsString( "object" ),
          "required" -> JsArray( Seq( "id", "types" ).map( JsString ) ),
          "properties" -> JsObject( Seq(
            "id" -> JsObject( Seq(
              "type" -> JsString( "integer" ),
              "format" -> JsString( "int64" )
            ) ),
            "types" -> JsObject( Seq(
              "type" -> JsString( "array" ),
              "items" -> JsObject( Seq(
                "type" -> JsString( "string" )
              ) )
            ) )
          ) )
        ) ),
        JsObject( Seq( "$ref" -> JsString( "#/definitions/HasVertexProperties" ) ) )
      ) )
    ) )
  }

  def newVertexSchema: JsObject = {
    JsObject( Seq(
      "allOf" -> JsArray( Seq(
        JsObject( Seq(
          "type" -> JsString( "object" ),
          "required" -> JsArray( Seq( "temp_id", "types" ).map( JsString ) ),
          "properties" -> JsObject( Seq(
            "temp_id" -> JsObject( Seq(
              "type" -> JsString( "integer" ),
              "format" -> JsString( "int32" )
            ) ),
            "types" -> JsObject( Seq(
              "type" -> JsString( "array" ),
              "items" -> JsObject( Seq(
                "type" -> JsString( "string" )
              ) )
            ) )
          ) )
        ) ),
        JsObject( Seq( "$ref" -> JsString( "#/definitions/HasBaseVertexProperties" ) ) )
      ) )
    ) )
  }

}
