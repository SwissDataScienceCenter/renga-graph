lazy val commonSettings = Seq(
  organization := "ch.datascience",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.8"
)

//lazy val projectName = "renga-graph"
name := "renga-graph"

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    ourScalariformPreferences
  ).aggregate(
    core,
    `typesystem-implementation`
  )

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    ourScalariformPreferences
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
