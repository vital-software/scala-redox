import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

organization := "com.github.mdcollab"

name := "scala-redox"

scalaVersion := "2.12.10"

githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN")
githubOwner := "mdcollab"
githubRepository := "scala-redox"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.jcenterRepo,
  "Typesafe Repo" at "https://repo.typesafe.com/typesafe/releases/",
  "Typesafe Repo" at "https://repo.typesafe.com/typesafe/releases/",
  "Atlassian Releases" at "https://maven.atlassian.com/public/",
)

val playJsonVersion = "2.7.3"
val playVersion = "2.7.3"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % playJsonVersion,
  "com.typesafe.play" %% "play-logback" % playJsonVersion,
  "com.typesafe.play" %% "play-json-joda" % playJsonVersion,
  "com.typesafe.play" %% "play-ahc-ws" % playVersion,
  "com.typesafe.play" %% "play-ws-standalone-json" % "1.1.2",
  "com.typesafe.akka" %% "akka-http" % "10.1.8",
  "com.github.vital-software" %% "json-annotation" % "0.6.0",
  "com.github.nscala-time" %% "nscala-time" % "2.14.0",
  "com.typesafe.play" %% "play-specs2" % playVersion % Test,
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0"  cross CrossVersion.full)

ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation")

publishMavenStyle := true

pomIncludeRepository := { _ =>
  false
}
