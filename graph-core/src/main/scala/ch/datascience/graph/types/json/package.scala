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

package ch.datascience.graph.types

import ch.datascience.graph.naming.json.NamespaceAndNameFormat
import play.api.libs.json.Format

/**
 * Created by johann on 23/05/17.
 */
package object json {

  //  lazy val propKeyReads: Reads[PropertyKey#Key] = NamespaceAndNameReads
  //  lazy val propKeyWrites: Writes[PropertyKey#Key] = NamespaceAndNameWrites

  lazy val propKeyFormat: Format[PropertyKey#Key] = NamespaceAndNameFormat

  //  implicit lazy val propertyKeyReads: Reads[PropertyKey] = new PropertyKeyReads
  //  implicit lazy val propertyKeyWrites: Writes[PropertyKey] = new PropertyKeyWrites

}
