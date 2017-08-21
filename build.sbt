lazy val commonSettings = Seq(
  organization := "ch.datascience",
  version := "0.1.0-SNAPSHOT",
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
    `typesystem-service`
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
  `mutation-implementation`
).enablePlugins(
  PlayScala
)

lazy val `navigation-service` = (project in file("navigation/service"))
  .settings(
    commonSettings,
    ourScalariformPreferences
  ).dependsOn(
    core
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
    `typesystem-implementation`
  ).enablePlugins(
    PlayScala
  )

// Source code formatting
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

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
  publishTo := {
    val nexus = "https://testing.datascience.ch:18081/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "repository/maven-snapshots/")
    else
      None //TODO
  },
  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
)
