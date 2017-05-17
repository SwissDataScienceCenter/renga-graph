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

package ch.datascience.graph.scope.persistence.remote

import ch.datascience.graph.scope.persistence.PersistedNamedTypes
import ch.datascience.graph.types.NamedType

import scala.concurrent.Future

/**
  * Created by johann on 17/05/17.
  */
trait RemotePersistedNamedTypes[TypeKey, PropKey] extends PersistedNamedTypes[TypeKey, PropKey] {

  /**
    * Fetches named type with specified key
    *
    * @param key
    * @return a future containing some named type if a corresponding one is found, None otherwise
    */
  final def fetchNamedTypeFor(key: TypeKey): Future[Option[NamedType[TypeKey, PropKey]]] = ???

  /**
    * Grouped version of fetchNamedTypeFor
    *
    * If some keys are not found, they will not be part of the result map
    *
    * @param keys set of keys to retrieve
    * @return map key -> named type, will not contain unknown keys
    */
  final def fetchNamedTypesFor(keys: Set[TypeKey]): Future[Map[TypeKey, NamedType[TypeKey, PropKey]]] = ???

}
