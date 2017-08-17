package ch.datascience.graph.scope.persistence.dummy

import ch.datascience.graph.scope.Scope
import ch.datascience.graph.types.{ NamedType, PropertyKey }

trait DummyScope extends Scope {

  def addPropertyDefinitions( definitions: Iterable[( PropertyKey#Key, PropertyKey )] ): this.type = {
    for ( ( key, propertyKey ) <- definitions ) {
      this.propertyDefinitions.put( key, propertyKey )
    }

    this
  }

  def addNamedTypeDefinitions( definitions: Iterable[( NamedType#TypeId, NamedType )] ): this.type = {
    for ( ( key, namedType ) <- definitions ) {
      this.namedTypeDefinitions.put( key, namedType )
    }

    this
  }

}
