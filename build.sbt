organization := "com.github.vital-software"

name := "scala-redox"

version := "0.4-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.0-M6",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.0.0-RC1",
  "com.typesafe.akka" %% "akka-http" % "10.0.1",
  //"ai.x" %% "play-json-extensions" % "0.8.0",
  "com.iheart" %% "ficus" % "1.4.0",
  //"systems.uom" % "systems-common-java8" % "0.6",
  //"systems.uom" % "systems-unicode-java8" % "0.6",
  "com.github.vital-software" %% "json-annotation" % "0.3",
  "com.github.nscala-time" %% "nscala-time" % "2.14.0",
  "org.specs2" %% "specs2" % "2.3.13" % Test
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

sonatypeProfileName := "com.github.vital-software"

pomExtra := (
  <url>https://github.com/vital-software/scala-redox</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>http://opensource.org/licenses/MIT</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:vital-software/scala-redox.git</url>
    <connection>scm:git:git@github.com:vital-software/scala-redox.git</connection>
  </scm>
  <developers>
    <developer>
      <id>apatzer</id>
      <name>Aaron Patzer</name>
      <url>https://github.com/apatzer</url>
    </developer>
  </developers>)
