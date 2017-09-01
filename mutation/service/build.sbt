name := "renga-graph-mutation-service"

lazy val play_slick_version = "2.1.0"
lazy val rabbitmq_version = "4.1.0"
lazy val postgresql_version = "42.0.0"

libraryDependencies += filters
libraryDependencies += "com.typesafe.play" %% "play-slick" % play_slick_version
libraryDependencies += "org.postgresql" % "postgresql" % postgresql_version

lazy val scalatestplus_play_version = "2.0.0"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % scalatestplus_play_version % Test

lazy val janusgraph_version = "0.1.0"
libraryDependencies += "org.janusgraph" % "janusgraph-cassandra" % janusgraph_version //% Runtime

libraryDependencies += "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.8.9"


import com.typesafe.sbt.packager.docker._
dockerBaseImage := "openjdk:8-jre-alpine"
dockerCommands ~= { cmds => cmds.head +: ExecCmd("RUN", "apk", "add", "--no-cache", "bash") +: cmds.tail }
