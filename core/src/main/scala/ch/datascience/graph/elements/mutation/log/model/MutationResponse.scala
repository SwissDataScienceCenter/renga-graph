package ch.datascience.graph.elements.mutation.log.model

import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.libs.functional.syntax._

sealed abstract class MutationResponse( val status: MutationResponse.MutationStatus )
case class MutationSuccess( results: Seq[JsObject] ) extends MutationResponse( status = MutationResponse.Success )
case class MutationFailed( reason: String ) extends MutationResponse( status = MutationResponse.Failed )

object MutationResponse {

  sealed abstract class MutationStatus {
    override final def toString: String = this match {
      case Success => "success"
      case Failed  => "failed"
    }
  }
  case object Success extends MutationStatus
  case object Failed extends MutationStatus

  object MutationStatus {
    def valueOf( str: String ): MutationStatus = str.toLowerCase match {
      case "success" => Success
      case "failed"  => Failed
    }

    implicit lazy val format: Format[MutationStatus] = Format( reads, writes )

    private[this] def reads: Reads[MutationStatus] = JsPath.read[String].map( _.toLowerCase ).collect( ValidationError( "expects either success or failed" ) ) {
      case "success" => Success
      case "failed"  => Failed
    }

    private[this] def writes: Writes[MutationStatus] = Writes { status: MutationStatus => JsString( status.toString ) }
  }
}
