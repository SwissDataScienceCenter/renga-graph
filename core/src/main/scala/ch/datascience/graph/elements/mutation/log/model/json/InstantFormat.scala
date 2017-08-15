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

package ch.datascience.graph.elements.mutation.log.model.json

import java.time.format.{ DateTimeFormatter, DateTimeParseException }
import java.time.{ Instant, ZonedDateTime }

import play.api.libs.json._

/**
 * Created by johann on 28/06/17.
 */
object InstantFormat extends Format[Instant] {

  override def writes( t: Instant ): JsValue = JsString( t.atZone( java.time.ZoneId.of( "UTC" ) ).format( formatter ) )

  override def reads( json: JsValue ): JsResult[Instant] = implicitly[Reads[String]].reads( json ).flatMap { str =>
    try {
      JsSuccess( ZonedDateTime.parse( str, formatter ).toInstant )
    }
    catch {
      case e: DateTimeParseException => JsError( e.getMessage )
    }
  }

  lazy val formatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

}
