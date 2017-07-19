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

organization := "ch.datascience"
name := "graph-typesystem-service"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.11.8"

lazy val root = Project(
  id   = "graph-typesystem-service",
  base = file(".")
).dependsOn(
  core,
  typesystemPersistence
).settings(
  projectDependencies +=
    (projectID in typesystemPersistence).value.exclude("org.slf4j", "slf4j-log4j12").exclude("org.slf4j", "slf4j-nop")
).enablePlugins(
  PlayScala
)

lazy val core = RootProject(file("../graph-core"))

lazy val typesystemPersistence = RootProject(file("../graph-typesystem-persistence"))

resolvers += DefaultMavenRepository

lazy val play_slick_version = "2.1.0"
lazy val postgresql_version = "42.0.0"

libraryDependencies += filters
libraryDependencies += "com.typesafe.play" %% "play-slick" % play_slick_version
libraryDependencies += "org.postgresql" % "postgresql" % postgresql_version

// Runtime dependencies (runtime removed to load them when sbt console; I am too lazy to redefine console)
lazy val h2_version = "1.4.193"
lazy val janusgraph_version = "0.1.0"

libraryDependencies += "org.janusgraph" % "janusgraph-cassandra" % janusgraph_version //% Runtime
//libraryDependencies += "com.h2database" % "h2" % h2_version //% Runtime

lazy val scalatestplus_play_version = "2.0.0"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % scalatestplus_play_version % Test

lazy val initDB = taskKey[Unit]("Initialize database")

fullRunTask(initDB, Runtime, "init.Main")

import com.typesafe.sbt.packager.docker._

dockerBaseImage := "openjdk:8-jre-alpine"

dockerCommands ~= { cmds => cmds.head +: ExecCmd("RUN", "apk", "add", "--no-cache", "bash") +: cmds.tail }
// Replace entry point
dockerCommands ~= { cmds =>
  cmds.map {
    case ExecCmd("ENTRYPOINT", args@_*) => ExecCmd("ENTRYPOINT", args ++ Seq("-Dconfig.resource=application.docker.conf"): _*)
    case cmd => cmd
  }
}
