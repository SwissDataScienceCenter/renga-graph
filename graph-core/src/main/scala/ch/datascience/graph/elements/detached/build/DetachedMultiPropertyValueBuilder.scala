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

package ch.datascience.graph.elements.detached.build

import ch.datascience.graph.elements.{ListValue, MultiPropertyValue, SetValue, SingleValue}
import ch.datascience.graph.elements.detached.DetachedRichProperty
import ch.datascience.graph.types.Cardinality

import scala.collection.mutable

/**
  * Created by johann on 05/07/17.
  */
sealed abstract class DetachedMultiPropertyValueBuilder(val cardinality: Cardinality) {

  def key: DetachedRichProperty#Key

  def result(): MultiPropertyValue[DetachedRichProperty]

}

class DetachedSingleValueBuilder(
  val key: DetachedRichProperty#Key,
  val value: DetachedRichProperty#Value
) extends DetachedMultiPropertyValueBuilder(cardinality = Cardinality.Single) {

  val property: DetachedRichPropertyBuilder = new DetachedRichPropertyBuilder(key, value)

  def result(): SingleValue[DetachedRichProperty] = SingleValue(property.result())

}

class DetachedSetValueBuilder(val key: DetachedRichProperty#Key) extends DetachedMultiPropertyValueBuilder(cardinality = Cardinality.Set) {

  val properties: mutable.Buffer[DetachedRichPropertyBuilder] = mutable.Buffer.empty[DetachedRichPropertyBuilder]

  def addProperty(prop: DetachedRichPropertyBuilder): this.type = {
    properties += prop
    this
  }

  def result(): SetValue[DetachedRichProperty] = SetValue(properties.toList.map(_.result()))

}

class DetachedListValueBuilder(val key: DetachedRichProperty#Key) extends DetachedMultiPropertyValueBuilder(cardinality = Cardinality.List) {

  val properties: mutable.Buffer[DetachedRichPropertyBuilder] = mutable.Buffer.empty[DetachedRichPropertyBuilder]

  def addProperty(prop: DetachedRichPropertyBuilder): this.type = {
    properties += prop
    this
  }

  def result(): ListValue[DetachedRichProperty] = ListValue(properties.toList.map(_.result()))

}