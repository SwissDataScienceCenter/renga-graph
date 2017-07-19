organization := "ch.datascience"
name := "graph-init"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.11.8"

lazy val root = Project(
  id   = "graph-init",
  base = file(".")
).dependsOn(
  core,
  typesystemPersistence
).enablePlugins(
  JavaAppPackaging
)

lazy val core = RootProject(file("../graph-core"))

lazy val typesystemPersistence = RootProject(file("../graph-typesystem-persistence"))

resolvers += DefaultMavenRepository

import com.typesafe.sbt.packager.docker._

dockerBaseImage := "openjdk:8-jre-alpine"

dockerCommands ~= { cmds => cmds.head +: ExecCmd("RUN", "apk", "add", "--no-cache", "bash") +: cmds.tail }
