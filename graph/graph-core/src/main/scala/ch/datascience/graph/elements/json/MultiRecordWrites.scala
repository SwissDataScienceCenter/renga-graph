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

package ch.datascience.graph.elements.json

import ch.datascience.graph.elements.{MultiPropertyValue, MultiRecord, Property}
import play.api.libs.json.{JsPath, JsValue, Writes}

/**
  * Created by johann on 31/05/17.
  */
class MultiRecordWrites[P <: Property : Writes] extends Writes[MultiRecord { type Prop <: P }] {

  def writes(record: MultiRecord { type Prop <: P }): JsValue = (JsPath \ "properties").write[record.Properties].writes(record.properties)

//  private[this] implicit lazy val mapWrites: Writes[Map[P#Key, MultiPropertyValue[P]]] = KeyFormat.mapWrites[MultiPropertyValue[P]](multiPropertyValueWrites)
  private[this] implicit lazy val mapWrites: Writes[Map[P#Key, MultiPropertyValue[P]]] = new Writes[Map[P#Key, MultiPropertyValue[P]]] {
    def writes(m: Map[P#Key, MultiPropertyValue[P]]): JsValue = implicitly[Writes[Iterable[MultiPropertyValue[P]]]].writes(m.values)
  }

  private[this] implicit lazy val multiPropertyValueWrites: MultiPropertyValueWrites[P] = new MultiPropertyValueWrites[P]

}
