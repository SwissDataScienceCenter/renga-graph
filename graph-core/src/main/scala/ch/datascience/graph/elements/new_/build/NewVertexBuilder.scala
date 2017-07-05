package ch.datascience.graph.elements.new_.build

import ch.datascience.graph.elements.MultiPropertyValue
import ch.datascience.graph.elements.detached.build._
import ch.datascience.graph.elements.new_.NewVertex

import scala.collection.mutable

/**
  * Created by johann on 05/07/17.
  */
class NewVertexBuilder(val tempId: NewVertex#TempId) {

  def this() {
    this(tempId = 0)
  }

  val types: mutable.Set[NewVertex#TypeId] = mutable.Set.empty[NewVertex#TypeId]

  val properties: mutable.Map[NewVertex#Prop#Key, DetachedMultiPropertyValueBuilder] =  mutable.Map.empty[NewVertex#Prop#Key, DetachedMultiPropertyValueBuilder]

  def result(): NewVertex = NewVertex(tempId, types.toSet, properties.toMap.mapValues(_.result()))

  def addType(typeId: NewVertex#TypeId): this.type = {
    types += typeId
    this
  }

  def addProperty(prop: DetachedMultiPropertyValueBuilder): this.type = {
    properties += prop.key -> prop
    this
  }

  def addSingleProperty(key: NewVertex#Prop#Key, value: NewVertex#Prop#Value): this.type = {
    addProperty(new DetachedSingleValueBuilder(key, value))
  }

  def addSetProperty(key: NewVertex#Prop#Key): this.type = {
    addProperty(new DetachedSetValueBuilder(key))
  }

  def addSetProperty(key: NewVertex#Prop#Key, value: NewVertex#Prop#Value): this.type = {
    if (!properties.contains(key)) {
      addSetProperty(key: NewVertex#Prop#Key)
    }
    val multiProp = properties(key)
    multiProp match {
      case p: DetachedSetValueBuilder => p.addProperty(new DetachedRichPropertyBuilder(key, value))
      case _ => throw new IllegalArgumentException(s"Bad cardinality, expected ${multiProp.cardinality} is not Set")
    }
    this
  }

  def addListProperty(key: NewVertex#Prop#Key): this.type = {
    addProperty(new DetachedListValueBuilder(key))
  }

  def addListProperty(key: NewVertex#Prop#Key, value: NewVertex#Prop#Value): this.type = {
    if (!properties.contains(key)) {
      addListProperty(key: NewVertex#Prop#Key)
    }
    val multiProp = properties(key)
    multiProp match {
      case p: DetachedListValueBuilder => p.addProperty(new DetachedRichPropertyBuilder(key, value))
      case _ => throw new IllegalArgumentException(s"Bad cardinality, expected ${multiProp.cardinality} is not List")
    }
    this
  }

}
