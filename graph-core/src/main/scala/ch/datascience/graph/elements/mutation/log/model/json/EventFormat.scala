package ch.datascience.graph.elements.mutation.log.model.json

import java.time.Instant
import java.util.UUID

import ch.datascience.graph.elements.mutation.log.model.Event
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by johann on 28/06/17.
  */
object EventFormat extends Format[Event] {

  override def writes(event: Event): JsValue = self.writes(event)

  override def reads(json: JsValue): JsResult[Event] = self.reads(json)

  private[this] implicit lazy val self: Format[Event] = (
    (JsPath \ "uuid").format[UUID] and
      (JsPath \ "event").format[JsValue] and
      (JsPath \ "timestamp").format[Instant]
    )(Event.apply, unlift(Event.unapply))

}
