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
