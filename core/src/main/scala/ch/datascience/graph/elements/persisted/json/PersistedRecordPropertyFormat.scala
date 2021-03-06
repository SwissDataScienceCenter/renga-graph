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

package ch.datascience.graph.elements.persisted.json

import ch.datascience.graph.elements.Property
import ch.datascience.graph.elements.json.PropertyFormat
import ch.datascience.graph.elements.persisted.{ Path, PersistedRecordProperty }
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Created by johann on 13/06/17.
 */
object PersistedRecordPropertyFormat extends Format[PersistedRecordProperty] {

  def writes( prop: PersistedRecordProperty ): JsValue = writer.writes( prop )

  def reads( json: JsValue ): JsResult[PersistedRecordProperty] = reader.reads( json )

  private[this] lazy val writer: Writes[PersistedRecordProperty] = (
    ( JsPath \ "parent" ).write[Path]( PathMappers.PathFormat ) and
    JsPath.write[Property]( PropertyFormat )
  ) { prop => ( prop.parent, prop ) }

  private[this] lazy val reader: Reads[PersistedRecordProperty] = (
    ( JsPath \ "parent" ).read[Path]( PathMappers.PathFormat ) and
    JsPath.read[Property]( PropertyFormat )
  ) { ( parent, prop ) => PersistedRecordProperty( parent, prop.key, prop.value ) }

}
