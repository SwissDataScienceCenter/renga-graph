package controllers

import java.io.InputStream

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import play.api.libs.json.JsValue
import play.api.libs.json.jackson.PlayJsonModule

object YamlHelper {

  /**
   * Converts yaml InputStream to a JsValue object
   * @param is yaml file as an InputStream
   * @return json value as JsValue
   */
  def convertYamlToJson( is: InputStream ): JsValue = {
    yamlReader.readValue( is, classOf[JsValue] )
  }

  /**
   * YAML reader with Play's JSON module enabled
   */
  def yamlReader: ObjectMapper = {
    new ObjectMapper( new YAMLFactory() ).registerModule( PlayJsonModule )
  }

}
