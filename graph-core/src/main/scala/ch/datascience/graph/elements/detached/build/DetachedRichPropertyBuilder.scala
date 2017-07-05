package ch.datascience.graph.elements.detached.build

import ch.datascience.graph.elements.detached.{DetachedProperty, DetachedRichProperty}

import scala.collection.mutable

/**
  * Created by johann on 05/07/17.
  */
class DetachedRichPropertyBuilder(val key: DetachedRichProperty#Key,
                                  val value: DetachedRichProperty#Value) {

  val properties: mutable.Map[DetachedRichProperty#Prop#Key, DetachedRichProperty#Prop] = mutable.Map.empty[DetachedRichProperty#Prop#Key, DetachedRichProperty#Prop]

  def result(): DetachedRichProperty = DetachedRichProperty(key, value, properties.toMap)

  def addProperty(prop: DetachedRichProperty#Prop): this.type = {
    properties += prop.key -> prop
    this
  }

  def addProperty(key: DetachedRichProperty#Prop#Key, value: DetachedRichProperty#Prop#Value): this.type = {
    addProperty(DetachedProperty(key, value))
  }

}
