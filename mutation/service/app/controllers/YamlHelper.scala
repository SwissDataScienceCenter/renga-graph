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
