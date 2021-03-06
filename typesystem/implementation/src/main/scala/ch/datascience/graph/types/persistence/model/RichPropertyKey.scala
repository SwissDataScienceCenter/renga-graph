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

package ch.datascience.graph.types.persistence.model

import java.util.UUID

import ch.datascience.graph.naming.NamespaceAndName
import ch.datascience.graph.types.{ Cardinality, DataType }
import ch.datascience.graph.types.{ PropertyKey => StandardPropertyKey }

/**
 * Created by johann on 23/05/17.
 */
class RichPropertyKey(
    id:              UUID,
    val graphDomain: GraphDomain,
    name:            String,
    dataType:        DataType,
    cardinality:     Cardinality
) extends PropertyKey(
  id,
  graphDomain.id,
  name,
  dataType,
  cardinality
) with RichAbstractEntity[PropertyKey] {

  def key: NamespaceAndName = NamespaceAndName( graphDomain.namespace, name )

  def toStandardPropertyKey: StandardPropertyKey = StandardPropertyKey( key, dataType, cardinality )

}

object RichPropertyKey {

  def apply( id: UUID, graphDomain: GraphDomain, name: String, dataType: DataType, cardinality: Cardinality ): RichPropertyKey = {
    new RichPropertyKey( id, graphDomain, name, dataType, cardinality )
  }

  def unapply( propertyKey: RichPropertyKey ): Option[( UUID, GraphDomain, String, DataType, Cardinality )] = {
    if ( propertyKey eq null )
      None
    else
      Some( ( propertyKey.id, propertyKey.graphDomain, propertyKey.name, propertyKey.dataType, propertyKey.cardinality ) )
  }

}
