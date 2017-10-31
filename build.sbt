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

lazy val commonSettings = Seq(
  organization := "ch.datascience",
  version := "0.1.1-SNAPSHOT",
  scalaVersion := "2.11.8"
) ++ publishSettings

//lazy val projectName = "renga-graph"
name := "renga-graph"

// This project contains nothing to package, like pure POM maven project
packagedArtifacts := Map.empty

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    ourScalariformPreferences
  ).aggregate(
    core,
    init,
    `mutation-implementation`,
    `mutation-service`,
    `navigation-service`,
    `typesystem-implementation`,
    `typesystem-service`,
    utils
  )

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    ourScalariformPreferences
  )

lazy val init = (project in file("init"))
  .settings(
    commonSettings,
    ourScalariformPreferences
  ).dependsOn(
    core,
    `typesystem-implementation`
  ).enablePlugins(
    JavaAppPackaging
  )

lazy val `mutation-implementation` = (project in file("mutation/implementation"))
  .settings(
    commonSettings,
    ourScalariformPreferences
  ).dependsOn(
    core
  )

lazy val `mutation-service` = (project in file("mutation/service"))
  .settings(
    commonSettings,
    ourScalariformPreferences,
    projectDependencies +=
      (projectID in `mutation-implementation`).value.exclude("org.slf4j", "slf4j-log4j12").exclude("org.slf4j", "slf4j-nop")
  ).dependsOn(
    core,
    `mutation-implementation`,
    utils
  ).enablePlugins(
    PlayScala
  )

lazy val `navigation-service` = (project in file("navigation/service"))
  .settings(
    commonSettings,
    ourScalariformPreferences
  ).dependsOn(
    core,
    utils
  ).enablePlugins(
    PlayScala
  )

lazy val `typesystem-implementation` = (project in file("typesystem/implementation"))
  .settings(
    commonSettings,
    ourScalariformPreferences
  ).dependsOn(
    core
  )

lazy val `typesystem-service` = (project in file("typesystem/service"))
  .settings(
    commonSettings,
    ourScalariformPreferences,
    projectDependencies +=
      (projectID in `typesystem-implementation`).value.exclude("org.slf4j", "slf4j-log4j12").exclude("org.slf4j", "slf4j-nop")
  ).dependsOn(
    core,
    `typesystem-implementation`,
    utils
  ).enablePlugins(
    PlayScala
  )

lazy val utils = (project in file("utils"))
  .settings(
    commonSettings,
    ourScalariformPreferences
  ).dependsOn(
  core
  )

// Source code formatting
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

val ourScalariformPreferences =
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference( AlignArguments,                               true  )
    .setPreference( AlignParameters,                              true  )
    .setPreference( AlignSingleLineCaseStatements,                true  )
    .setPreference( AlignSingleLineCaseStatements.MaxArrowIndent, 40    )
    .setPreference( CompactControlReadability,                    true  )
    .setPreference( CompactStringConcatenation,                   false )
    .setPreference( DanglingCloseParenthesis,                     Force )
    .setPreference( DoubleIndentConstructorArguments,             true  )
    .setPreference( DoubleIndentMethodDeclaration,                true  )
    .setPreference( FirstArgumentOnNewline,                       Force )
    .setPreference( FirstParameterOnNewline,                      Force )
    .setPreference( FormatXml,                                    true  )
    .setPreference( IndentPackageBlocks,                          true  )
    .setPreference( IndentSpaces,                                 2     )
    .setPreference( IndentWithTabs,                               false )
    .setPreference( MultilineScaladocCommentsStartOnFirstLine,    false )
    .setPreference( NewlineAtEndOfFile,                           true  )
    .setPreference( PlaceScaladocAsterisksBeneathSecondAsterisk,  false )
    .setPreference( PreserveSpaceBeforeArguments,                 false )
    .setPreference( RewriteArrowSymbols,                          false )
    .setPreference( SpaceBeforeColon,                             false )
    .setPreference( SpaceBeforeContextColon,                      true  )
    .setPreference( SpaceInsideBrackets,                          false )
    .setPreference( SpaceInsideParentheses,                       true  )
    .setPreference( SpacesAroundMultiImports,                     true  )
    .setPreference( SpacesWithinPatternBinders,                   false )

SbtScalariform.scalariformSettings ++ Seq(ourScalariformPreferences)

// Publishing
lazy val publishSettings = Seq(
  sonatypeProfileName := "ch.datascience",
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  licenses := Seq("The Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  homepage := Some(url("https://datascience.ch/renga-platform/")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/SwissDataScienceCenter/renga-graph"),
      "scm:git@github.com:SwissDataScienceCenter/renga-graph.git"
    )
  ),
  developers := List(
    Developer(id="leafty", name="Johann-Michael Thiebaut", email="johann.thiebaut@gmail.com", url=url("https://github.com/leafty"))
  ),
  publishTo := {
    if (isSnapshot.value)
      Some(Opts.resolver.sonatypeSnapshots)
    else
      Some(Opts.resolver.sonatypeStaging)
  },
  publishArtifact in Test := false
) ++ (
  for {
    username <- sys.env.get("SONATYPE_USERNAME")
    password <- sys.env.get("SONATYPE_PASSWORD")
  } yield credentials += Credentials(
    "Sonatype Nexus Repository Manager",
    "oss.sonatype.org",
    username,
    password)
  ).toSeq

pgpPublicRing := baseDirectory.value / "project" / ".gnupg" / "pubring.gpg"
pgpSecretRing := baseDirectory.value / "project" / ".gnupg" / "secring.gpg"
