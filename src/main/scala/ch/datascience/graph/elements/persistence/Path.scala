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

package ch.datascience.graph.elements.persistence

/**
  * Created by johann on 11/05/17.
  */
sealed abstract class Path

final case class VertexPath[+VertexId](vertexId: VertexId) extends MultiRecordPath

// TODO: case class EdgePath

sealed trait RecordPath extends Path

sealed trait MultiRecordPath extends Path

sealed abstract class PropertyPath extends Path {
  def parent: Path
}

case class PropertyPathFromRecord[+Key](parent: RecordPath, key: Key) extends PropertyPath

case class PropertyPathFromMultiRecord[+PropertyId](
  parent: MultiRecordPath,
  propertyId: PropertyId
) extends PropertyPath

final class RichPropertyPathFromRecord[+Key](parent: RecordPath, key: Key)
  extends PropertyPathFromRecord(parent, key) with RecordPath

final class RichPropertyPathFromMultiRecord[+PropertyId](parent: MultiRecordPath, propertyId: PropertyId)
  extends PropertyPathFromMultiRecord(parent, propertyId) with RecordPath
