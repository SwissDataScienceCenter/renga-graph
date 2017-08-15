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

name := "renga-graph-core"

//organization := "ch.datascience"
//name := "graph-core"
//version := "0.1.0-SNAPSHOT"
//scalaVersion := "2.11.8"
//
//resolvers += DefaultMavenRepository
//
lazy val play_version = "2.5.14"
lazy val tinkerpop_version = "3.2.3"

libraryDependencies += "com.typesafe.play" %% "play-json" % play_version
libraryDependencies += "com.typesafe.play" %% "play-ws" % play_version
libraryDependencies += "org.apache.tinkerpop" % "gremlin-core" % tinkerpop_version

lazy val scalatest_version = "3.0.1"

libraryDependencies += "org.scalatest" %% "scalatest" % scalatest_version % Test
//
//// Source code formatting
//import scalariform.formatter.preferences._
//import com.typesafe.sbt.SbtScalariform
//import com.typesafe.sbt.SbtScalariform.ScalariformKeys
//
//val preferences =
//  ScalariformKeys.preferences := ScalariformKeys.preferences.value
//    .setPreference( AlignArguments,                               true  )
//    .setPreference( AlignParameters,                              true  )
//    .setPreference( AlignSingleLineCaseStatements,                true  )
//    .setPreference( AlignSingleLineCaseStatements.MaxArrowIndent, 40    )
//    .setPreference( CompactControlReadability,                    true  )
//    .setPreference( CompactStringConcatenation,                   false )
//    .setPreference( DanglingCloseParenthesis,                     Force )
//    .setPreference( DoubleIndentConstructorArguments,             true  )
//    .setPreference( DoubleIndentMethodDeclaration,                true  )
//    .setPreference( FirstArgumentOnNewline,                       Force )
//    .setPreference( FirstParameterOnNewline,                      Force )
//    .setPreference( FormatXml,                                    true  )
//    .setPreference( IndentPackageBlocks,                          true  )
//    .setPreference( IndentSpaces,                                 2     )
//    .setPreference( IndentWithTabs,                               false )
//    .setPreference( MultilineScaladocCommentsStartOnFirstLine,    false )
//    .setPreference( NewlineAtEndOfFile,                           true  )
//    .setPreference( PlaceScaladocAsterisksBeneathSecondAsterisk,  false )
//    .setPreference( PreserveSpaceBeforeArguments,                 false )
//    .setPreference( RewriteArrowSymbols,                          false )
//    .setPreference( SpaceBeforeColon,                             false )
//    .setPreference( SpaceBeforeContextColon,                      true  )
//    .setPreference( SpaceInsideBrackets,                          false )
//    .setPreference( SpaceInsideParentheses,                       true  )
//    .setPreference( SpacesAroundMultiImports,                     true  )
//    .setPreference( SpacesWithinPatternBinders,                   false )
//
//SbtScalariform.scalariformSettings ++ Seq(preferences)
