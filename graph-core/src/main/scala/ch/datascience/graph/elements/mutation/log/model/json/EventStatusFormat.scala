package ch.datascience.graph.elements.mutation.log.model.json

import java.time.Instant
import java.util.UUID

import ch.datascience.graph.elements.mutation.log.model.{Event, EventStatus}
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Created by johann on 28/06/17.
  */
object EventStatusFormat extends Format[EventStatus] {

  override def writes(status: EventStatus): JsValue = writer.writes(status)

  override def reads(json: JsValue): JsResult[EventStatus] = reader.reads(json)

  private[this] implicit lazy val writer: Writes[EventStatus] = (
    (JsPath \ "request").write[Event](EventFormat) and
      (JsPath \ "status").write[String] and
      (JsPath \ "response").writeNullable[Event](EventFormat)
  ){ status =>
    status.status match {
      case EventStatus.Pending => (status.request, "pending", None)
      case EventStatus.Completed(res) => (status.request, "completed", Some(res))
    }
  }

  private[this] implicit lazy val reader: Reads[EventStatus] = (JsPath \ "status").read[String].flatMap{
    case "pending" => (
      (JsPath \ "request").read[Event](EventFormat) and
        (JsPath \ "status").read[String]
      ){ (request, _) => EventStatus(request.uuid, request, EventStatus.Pending) }
    case "completed" => (
      (JsPath \ "request").read[Event](EventFormat) and
        (JsPath \ "status").read[String] and
        (JsPath \ "response").read[Event](EventFormat)
      ){ (request, _, response) => EventStatus(request.uuid, request, EventStatus.Completed(response)) }
    case _ => Reads { json => JsError(s"Cannot parse: $json") }
  }

}
