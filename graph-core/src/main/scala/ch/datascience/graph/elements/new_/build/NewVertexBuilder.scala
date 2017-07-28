package ch.datascience.graph.elements.new_.build

import ch.datascience.graph.elements.MultiPropertyValue
import ch.datascience.graph.elements.detached.build._
import ch.datascience.graph.elements.new_.NewVertex
import ch.datascience.graph.naming.NamespaceAndName

import scala.collection.mutable

/**
 * Created by johann on 05/07/17.
 */
class NewVertexBuilder( val tempId: NewVertex#TempId ) {

  def this() {
    this( tempId = 0 )
  }

  val types: mutable.Set[NewVertex#TypeId] = mutable.Set.empty[NewVertex#TypeId]

  val properties: mutable.Map[NewVertex#Prop#Key, DetachedMultiPropertyValueBuilder] = mutable.Map.empty[NewVertex#Prop#Key, DetachedMultiPropertyValueBuilder]

  def result(): NewVertex = NewVertex( tempId, types.toSet, properties.toMap.mapValues( _.result() ) )

  def addType( typeId: NewVertex#TypeId ): this.type = {
    types += typeId
    this
  }

  def addProperty( prop: DetachedMultiPropertyValueBuilder ): this.type = {
    properties += prop.key -> prop
    this
  }

  def addSingleProperty( key: String, value: NewVertex#Prop#Value, block: ( DetachedRichPropertyBuilder ) => Unit ): this.type = {
    addSingleProperty( makeKey( key ), value, block )
  }

  def addSingleProperty( key: String, value: NewVertex#Prop#Value ): this.type = {
    addSingleProperty( key, value, { _ => () }: ( DetachedRichPropertyBuilder ) => Unit )
  }

  def addSingleProperty( key: NewVertex#Prop#Key, value: NewVertex#Prop#Value, block: ( DetachedRichPropertyBuilder ) => Unit ): this.type = {
    val builder = new DetachedSingleValueBuilder( key, value )
    addProperty( builder )
    block( builder.property )
    this
  }

  def addSingleProperty( key: NewVertex#Prop#Key, value: NewVertex#Prop#Value ): this.type = {
    addSingleProperty( key, value, { _ => () }: ( DetachedRichPropertyBuilder ) => Unit )
  }

  def addSetProperty( key: NewVertex#Prop#Key ): this.type = {
    addProperty( new DetachedSetValueBuilder( key ) )
  }

  def addSetProperty( key: NewVertex#Prop#Key, value: NewVertex#Prop#Value ): this.type = {
    if ( !properties.contains( key ) ) {
      addSetProperty( key: NewVertex#Prop#Key )
    }
    val multiProp = properties( key )
    multiProp match {
      case p: DetachedSetValueBuilder => p.addProperty( new DetachedRichPropertyBuilder( key, value ) )
      case _                          => throw new IllegalArgumentException( s"Bad cardinality, expected ${multiProp.cardinality} is not Set" )
    }
    this
  }

  def addSetProperty( key: NewVertex#Prop#Key, value: NewVertex#Prop#Value, block: ( DetachedRichPropertyBuilder ) => Unit ): this.type = {
    if ( !properties.contains( key ) ) {
      addSetProperty( key: NewVertex#Prop#Key )
    }
    val multiProp = properties( key )
    multiProp match {
      case p: DetachedSetValueBuilder => {
        val builder = new DetachedRichPropertyBuilder( key, value )
        p.addProperty( builder )
        block( builder )
      }
      case _ => throw new IllegalArgumentException( s"Bad cardinality, expected ${multiProp.cardinality} is not Set" )
    }
    this
  }

  def addListProperty( key: NewVertex#Prop#Key ): this.type = {
    addProperty( new DetachedListValueBuilder( key ) )
  }

  def addListProperty( key: NewVertex#Prop#Key, value: NewVertex#Prop#Value ): this.type = {
    if ( !properties.contains( key ) ) {
      addListProperty( key: NewVertex#Prop#Key )
    }
    val multiProp = properties( key )
    multiProp match {
      case p: DetachedListValueBuilder => p.addProperty( new DetachedRichPropertyBuilder( key, value ) )
      case _                           => throw new IllegalArgumentException( s"Bad cardinality, expected ${multiProp.cardinality} is not List" )
    }
    this
  }

  def addListProperty( key: NewVertex#Prop#Key, value: NewVertex#Prop#Value, block: ( DetachedRichPropertyBuilder ) => Unit ): this.type = {
    if ( !properties.contains( key ) ) {
      addListProperty( key: NewVertex#Prop#Key )
    }
    val multiProp = properties( key )
    multiProp match {
      case p: DetachedListValueBuilder => {
        val builder = new DetachedRichPropertyBuilder( key, value )
        p.addProperty( builder )
        block( builder )
      }
      case _ => throw new IllegalArgumentException( s"Bad cardinality, expected ${multiProp.cardinality} is not List" )
    }
    this
  }

  private[this] def makeKey( key: String ): NewVertex#Prop#Key = NamespaceAndName( key )

}
