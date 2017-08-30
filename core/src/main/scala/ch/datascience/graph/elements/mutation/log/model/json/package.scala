package ch.datascience.graph.elements.mutation.log.model

import play.api.libs.json.{ Format, OFormat }

/**
 * Created by johann on 28/06/17.
 */
package object json {

  implicit lazy val eventFormat: Format[Event] = EventFormat

  implicit lazy val eventStatusFormat: Format[EventStatus] = EventStatusFormat

  implicit lazy val mutationResponseFormat: OFormat[MutationResponse] = MutationResponseMappers.format

}
