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

object PropertySchema {

  def allSchemas: Map[String, JsObject] = {
    Map(
      "Property" -> propertySchema
    ) ++ typedPropertySchemas
  }

  def propertySchema: JsObject = JsObject( Seq(
    "type" -> JsString( "object" ),
    "required" -> JsArray( Seq( JsString( "key" ) ) ),
    "properties" -> JsObject( Seq(
      "key" -> JsObject( Seq(
        "type" -> JsString( "string" )
      ) )
    ) )
  ) )

  def valueType( dataType: DataType ): ( String, Option[String] ) = dataType match {
    case DataType.String    => ( "string", None )
    case DataType.Character => ( "string", None )
    case DataType.Boolean   => ( "boolean", None )
    case DataType.Byte      => ( "integer", Some( "int32" ) )
    case DataType.Short     => ( "integer", Some( "int32" ) )
    case DataType.Integer   => ( "integer", Some( "int32" ) )
    case DataType.Long      => ( "integer", Some( "int64" ) )
    case DataType.Float     => ( "number", Some( "float" ) )
    case DataType.Double    => ( "number", Some( "double" ) )
    case DataType.UUID      => ( "string", Some( "uuid" ) )
  }

  def valueSchema( dataType: DataType ): JsObject = {
    val ( dt, format ) = valueType( dataType )

    JsObject(
      Seq( "type" -> JsString( dt ) ) ++
        format.toSeq.map { f => "format" -> JsString( f ) }
    )
  }

  def typedPropertySchema( dataType: DataType ): JsObject = {
    JsObject( Seq(
      "allOf" -> JsArray( Seq(
        JsObject( Seq( "$ref" -> JsString( "#/definitions/Property" ) ) ),
        JsObject( Seq(
          "type" -> JsString( "object" ),
          "required" -> JsArray( Seq( JsString( "value" ) ) ),
          "properties" -> JsObject( Seq(
            "value" -> valueSchema( dataType )
          ) )
        ) )
      ) )
    ) )
  }

  def typedPropertySchemas: Map[String, JsObject] = {
    val seq = for {
      dt <- DataType.dataTypes
    } yield s"${dt.name.capitalize}Property" -> typedPropertySchema( dt )
    seq.toMap
  }

}
