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

/**
 * Created by johann on 14/04/17.
 */
sealed abstract class EntityState( val name: String )

object EntityState {

  def apply( name: String ): EntityState = name.toLowerCase match {
    case Pending.name  => Pending
    case Disabled.name => Disabled
    case Enabled.name  => Enabled
    case Failed.name   => Failed
  }

  case object Pending extends EntityState( name = "pending" )

  case object Disabled extends EntityState( name = "disabled" )

  case object Enabled extends EntityState( name = "enabled" )

  case object Failed extends EntityState( name = "failed" )

  def valueOf( name: String ): EntityState = EntityState.apply( name )

}
