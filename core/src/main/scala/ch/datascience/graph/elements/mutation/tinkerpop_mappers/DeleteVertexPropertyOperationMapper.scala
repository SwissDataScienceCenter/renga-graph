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

package ch.datascience.graph.elements.mutation.tinkerpop_mappers

import ch.datascience.graph.elements.mutation.delete.DeleteVertexPropertyOperation
import ch.datascience.graph.elements.tinkerpop_mappers.ValueWriter
import org.apache.tinkerpop.gremlin.process.traversal.Traverser
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.{ GraphTraversal, GraphTraversalSource }
import org.apache.tinkerpop.gremlin.structure.{ Vertex, VertexProperty }

/**
 * Created by johann on 20/06/17.
 */
object DeleteVertexPropertyOperationMapper extends Mapper {

  final type OperationType = DeleteVertexPropertyOperation

  final type Source = Vertex
  final type Element = Vertex

  def apply( op: DeleteVertexPropertyOperation ): ( GraphTraversalSource ) => GraphTraversal[Vertex, Vertex] = {
    val vertexProperty = op.vertexProperty

    { s: GraphTraversalSource =>
      // Get parent
      val t1 = PathHelper.follow( s, vertexProperty.path ).asInstanceOf[GraphTraversal[Vertex, VertexProperty[java.lang.Object]]]

      // Check validity of update
      val t2 = t1.map( updateFilter( ValueWriter.write( vertexProperty.value ) ) )

      t2.drop().iterate()

      val t3 = PathHelper.follow( s, vertexProperty.parent ).asInstanceOf[GraphTraversal[Vertex, Vertex]]

      t3
    }
  }

  private[this] type VertexPropertyType = OperationType#ElementType

  private[this] def updateFilter( oldValue: java.lang.Object ): java.util.function.Function[Traverser[VertexProperty[java.lang.Object]], VertexProperty[java.lang.Object]] = new java.util.function.Function[Traverser[VertexProperty[java.lang.Object]], VertexProperty[java.lang.Object]] {
    def apply( t: Traverser[VertexProperty[java.lang.Object]] ): VertexProperty[java.lang.Object] = {
      val vertexProperty = t.get()
      val storedValue = vertexProperty.value()
      if ( storedValue != oldValue )
        throw new IllegalArgumentException( s"Invalid updated: expected old value: $oldValue, but got $storedValue" )
      vertexProperty
    }
  }

}
