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

object EdgeSchema {

  def allSchemas: Map[String, JsObject] = {
    Map(
      "Edge" -> edgeSchema,
      "NewEdge" -> newEdgeSchema,
      "VertexReference" -> vertexReferenceSchema,
      "PersistedVertexReference" -> persistedVertexReferenceSchema,
      "NewVertexReference" -> newVertexReferenceSchema
    )
  }

  def edgeSchema: JsObject = {
    JsObject( Seq(
      "allOf" -> JsArray( Seq(
        JsObject( Seq(
          "type" -> JsString( "object" ),
          "required" -> JsArray( Seq( "id", "label", "from", "to" ).map( JsString ) ),
          "properties" -> JsObject( Seq(
            "id" -> JsObject( Seq(
              "type" -> JsString( "string" )
            ) ),
            "label" -> JsObject( Seq(
              "type" -> JsString( "string" )
            ) ),
            "from" -> JsObject( Seq(
              "type" -> JsString( "integer" ),
              "format" -> JsString( "int64" )
            ) ),
            "to" -> JsObject( Seq(
              "type" -> JsString( "integer" ),
              "format" -> JsString( "int64" )
            ) )
          ) )
        ) ),
        JsObject( Seq( "$ref" -> JsString( "#/definitions/HasProperties" ) ) )
      ) )
    ) )
  }

  def newEdgeSchema: JsObject = {
    JsObject( Seq(
      "allOf" -> JsArray( Seq(
        JsObject( Seq(
          "type" -> JsString( "object" ),
          "required" -> JsArray( Seq( "label", "from", "to" ).map( JsString ) ),
          "properties" -> JsObject( Seq(
            "label" -> JsObject( Seq(
              "type" -> JsString( "string" )
            ) ),
            "from" -> JsObject( Seq(
              "$ref" -> JsString( "#/definitions/VertexReference" )
            ) ),
            "to" -> JsObject( Seq(
              "$ref" -> JsString( "#/definitions/VertexReference" )
            ) )
          ) )
        ) ),
        JsObject( Seq( "$ref" -> JsString( "#/definitions/HasProperties" ) ) )
      ) )
    ) )
  }

  def vertexReferenceSchema: JsObject = {
    JsObject( Seq(
      "type" -> JsString( "object" ),
      "discriminator" -> JsString( "type" ),
      "required" -> JsArray( Seq( JsString( "type" ) ) ),
      "properties" -> JsObject( Seq(
        "type" -> JsObject( Seq(
          "type" -> JsString( "string" )
        ) )
      ) )
    ) )
  }

  def persistedVertexReferenceSchema: JsObject = {
    JsObject( Seq(
      "allOf" -> JsArray( Seq(
        JsObject( Seq( "$ref" -> JsString( "#/definitions/VertexReference" ) ) ),
        JsObject( Seq(
          "type" -> JsString( "object" ),
          "required" -> JsArray( Seq( "id" ).map( JsString ) ),
          "properties" -> JsObject( Seq(
            "id" -> JsObject( Seq(
              "type" -> JsString( "integer" ),
              "format" -> JsString( "int64" )
            ) ),
            "type" -> JsObject( Seq(
              "enum" -> JsArray( Seq( JsString( "persisted" ) ) )
            ) )
          ) )
        ) )
      ) )
    ) )
  }

  def newVertexReferenceSchema: JsObject = {
    JsObject( Seq(
      "allOf" -> JsArray( Seq(
        JsObject( Seq( "$ref" -> JsString( "#/definitions/VertexReference" ) ) ),
        JsObject( Seq(
          "type" -> JsString( "object" ),
          "required" -> JsArray( Seq( "id" ).map( JsString ) ),
          "properties" -> JsObject( Seq(
            "id" -> JsObject( Seq(
              "type" -> JsString( "integer" ),
              "format" -> JsString( "int32" )
            ) ),
            "type" -> JsObject( Seq(
              "enum" -> JsArray( Seq( JsString( "new" ) ) )
            ) )
          ) )
        ) )
      ) )
    ) )
  }

}
