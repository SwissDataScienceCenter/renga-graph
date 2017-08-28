package controllers

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

object SwaggerMappers {

  def updateHostAndSchemes( host: String, schemes: Seq[String] ): Reads[JsObject] = {
    updateHost( host ) andThen updateSchemes( schemes )
  }

  def updateHost( host: String ): Reads[JsObject] = {
    (
      ( JsPath \ "host" ).json.prune and
      ( JsPath \ "host" ).json.put( JsString( host ) )
    ).reduce
  }

  def updateSchemes( schemes: Seq[String] ): Reads[JsObject] = {
    (
      ( JsPath \ "schemes" ).json.prune and
      ( JsPath \ "schemes" ).json.put( JsArray( schemes.map( JsString ) ) )
    ).reduce
  }

}
