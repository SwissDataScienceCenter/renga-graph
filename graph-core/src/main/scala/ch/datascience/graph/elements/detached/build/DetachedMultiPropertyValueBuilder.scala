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