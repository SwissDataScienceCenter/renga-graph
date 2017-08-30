package ch.datascience.graph.elements.mutation.log.model.json

import ch.datascience.graph.elements.mutation.log.model.{ MutationFailed, MutationResponse, MutationSuccess }
import play.api.libs.functional.syntax._
import play.api.libs.json._

object MutationResponseMappers {

  implicit lazy val format: OFormat[MutationResponse] = OFormat( reads, writes )

  private[this] def reads: Reads[MutationResponse] = ( JsPath \ "status" ).read[MutationResponse.MutationStatus].flatMap {
    case MutationResponse.Success => ( JsPath \ "results" ).read[Seq[JsObject]].map( MutationSuccess )
    case MutationResponse.Failed  => ( JsPath \ "reason" ).read[String].map( MutationFailed )
  }

  private[this] def writes: OWrites[MutationResponse] = OWrites { response: MutationResponse =>
    response match {
      case r: MutationSuccess => writeSuccess.writes( r )
      case f: MutationFailed  => writeFailed.writes( f )
    }
  }

  private[this] def writeSuccess: OWrites[MutationSuccess] = (
    ( JsPath \ "status" ).write[MutationResponse.MutationStatus] and
    ( JsPath \ "results" ).write[Seq[JsObject]]
  ) { success: MutationSuccess => ( success.status, success.results ) }

  private[this] def writeFailed: OWrites[MutationFailed] = (
    ( JsPath \ "status" ).write[MutationResponse.MutationStatus] and
    ( JsPath \ "reason" ).write[String]
  ) { failed: MutationFailed => ( failed.status, failed.reason ) }

}
