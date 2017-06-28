organization := "ch.datascience"
name := "graph-typesystem-persistence"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.11.8"

lazy val root = Project(
  id   = "graph-typesystem-persistence",
  base = file(".")
).dependsOn(core)

lazy val core = RootProject(file("../graph-core"))

resolvers += DefaultMavenRepository
resolvers += "jitpack" at "https://jitpack.io"
resolvers += "Oracle Released Java Packages" at "http://download.oracle.com/maven"

lazy val slick_version = "3.2.0"
lazy val play_slick_version = "2.1.0"
lazy val janusgraph_version = "0.1.0"

libraryDependencies += "com.typesafe.slick" %% "slick" % slick_version
libraryDependencies += "com.typesafe.play" %% "play-slick" % play_slick_version
libraryDependencies += "org.janusgraph" % "janusgraph-core" % janusgraph_version

lazy val h2_version = "1.4.193"
lazy val scalatest_version = "3.0.1"

libraryDependencies += "com.h2database" % "h2" % h2_version % Test
libraryDependencies += "org.scalatest" %% "scalatest" % scalatest_version % Test

logBuffered in Test := false
parallelExecution in Test := false
