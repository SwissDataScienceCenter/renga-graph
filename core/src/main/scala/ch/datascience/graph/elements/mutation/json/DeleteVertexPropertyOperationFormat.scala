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

package ch.datascience.graph.elements.mutation.json

import ch.datascience.graph.elements.mutation.delete.DeleteVertexPropertyOperation
import ch.datascience.graph.elements.persisted.json.PersistedVertexPropertyFormat
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Created by johann on 20/06/17.
 */
object DeleteVertexPropertyOperationFormat extends OFormat[DeleteVertexPropertyOperation] {

  private[this] lazy val writer: OWrites[DeleteVertexPropertyOperation] = (
    ( JsPath \ "type" ).write[String] and
    ( JsPath \ "element" ).write[DeleteVertexPropertyOperation#ElementType]
  ) { op => ( "delete_vertex_property", op.vertexProperty ) }

  private[this] lazy val reader: Reads[DeleteVertexPropertyOperation] = (
    ( JsPath \ "type" ).read[String].filter( typeError )( _ == "delete_vertex_property" ) and
    ( JsPath \ "element" ).read[DeleteVertexPropertyOperation#ElementType]
  ) { ( _, vertexProperty ) => DeleteVertexPropertyOperation( vertexProperty ) }

  private[this] lazy val typeError = ValidationError( "expected type: 'delete_vertex_property'" )

  def writes( op: DeleteVertexPropertyOperation ): JsObject = writer.writes( op )

  def reads( json: JsValue ): JsResult[DeleteVertexPropertyOperation] = reader.reads( json )

  private[this] implicit lazy val newVertexFormat: Format[DeleteVertexPropertyOperation#ElementType] = PersistedVertexPropertyFormat

}
