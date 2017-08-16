name := "renga-graph-init"

//organization := "ch.datascience"
//name := "graph-init"
//version := "0.1.0-SNAPSHOT"
//scalaVersion := "2.11.8"
//
//lazy val root = Project(
//  id   = "graph-init",
//  base = file(".")
//).dependsOn(
//  core,
//  typesystemPersistence
//).enablePlugins(
//  JavaAppPackaging
//)
//
//lazy val core = RootProject(file("../graph-core"))
//
//lazy val typesystemPersistence = RootProject(file("../graph-typesystem-persistence"))
//
//resolvers += DefaultMavenRepository
//
// For packaging into a docker image

import com.typesafe.sbt.packager.docker._

dockerBaseImage := "openjdk:8-jre-alpine"

dockerCommands ~= { cmds => cmds.head +: ExecCmd("RUN", "apk", "add", "--no-cache", "bash") +: cmds.tail }
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
