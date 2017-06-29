package ch.datascience.graph.elements.mutation.log.model

import java.util.UUID

/**
  * Created by johann on 28/06/17.
  */
case class EventStatus(uuid : UUID, request: Event, status: EventStatus.Status)

object EventStatus {

  sealed abstract class Status

  case object Pending extends Status

  case class Completed(response: Event) extends Status

  def apply(request: Event, response: Option[Event]): EventStatus = {
    val uuid = request.uuid
    response match {
      case Some(res) =>
        require(uuid == res.uuid, s"Invalid request, response pair: $request, $res")
        EventStatus(uuid, request, EventStatus.Completed(res))
      case None => EventStatus(uuid, request, EventStatus.Pending)
    }
  }

}
