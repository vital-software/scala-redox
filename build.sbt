import java.nio.charset.StandardCharsets
import java.nio.file.Files

import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
import scalariform.formatter.preferences._

import scala.io.{ Codec, Source }
import scala.sys.process.ProcessLogger

organization := "com.github.vital-software"

name := "scala-redox"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.6")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.8",
  "com.typesafe.play" %% "play-json-joda" % "2.6.8",
  "com.typesafe.play" %% "play-ahc-ws" % "2.6.12",
  //"com.typesafe.play" %% "play-ahc-ws-standalone" % "1.1.6",
  "com.typesafe.play" %% "play-ws-standalone-json" % "1.1.6",
  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  //"ai.x" %% "play-json-extensions" % "0.8.0",
  "com.github.vital-software" %% "json-annotation" % "0.4.0",
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

// Scalariform settings
scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(FormatXml, false)
  .setPreference(DoubleIndentConstructorArguments, false)
  .setPreference(DanglingCloseParenthesis, Force)
// compile only unmanaged sources, not the generated (aka managed) sourced
sourceDirectories in (Compile, scalariformFormat) := (unmanagedSourceDirectories in Compile).value

// PGP settings
pgpPassphrase := Some(Array())
usePgpKeyHex("1bfe664d074b29f8")

// Release settings
releaseTagName              := s"${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}" // Remove v prefix
releaseTagComment           := s"Releasing ${(version in ThisBuild).value}\n\n[skip ci]"
releaseCommitMessage        := s"Setting version to ${(version in ThisBuild).value}\n\n[skip ci]"

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  setReleaseVersion,
  updateReleaseFiles,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

releasePublishArtifactsAction := PgpKeys.publishSigned.value

val updateReleaseFiles = ReleaseStep { state =>
  updateLine(
    state,
    "README.md",
    """libraryDependencies += "com.github.vital-software" %% "scala-redox" % """,
    v => s"""libraryDependencies += "com.github.vital-software" %% "scala-redox" % "$v""""
  )

  updateLine(
    state,
    "CHANGELOG.md",
    "## [Unreleased]",
    v => s"## [Unreleased]\n\n## [$v] - ${java.time.LocalDate.now}"
  )
}

def updateLine(state: State, fileName: String, marker: String, replacement: String => String): State = {
  val logger = new ProcessLogger {
    override def err(s: => String): Unit = state.log.info(s)
    override def out(s: => String): Unit = state.log.info(s)
    override def buffer[T](f: => T): T = state.log.buffer(f)
  }

  val vcs = Project.extract(state).get(releaseVcs).getOrElse {
    sys.error("VCS not set")
  }

  val (version: String, _) = state.get(ReleaseKeys.versions).getOrElse {
    sys.error(s"${ReleaseKeys.versions.label} key not set")
  }

  val fileToModify = Project.extract(state).get(baseDirectory.in(ThisBuild)) / fileName
  val lines = Source.fromFile(fileToModify)(Codec.UTF8).getLines().toList
  val lineNumber = lines.indexWhere(_.contains(marker))

  if (lineNumber == -1) {
    throw new RuntimeException(s"Could not find marker '$marker' in file '${fileToModify.getPath}'")
  }

  val content = lines.updated(lineNumber, replacement(version)).mkString("\n") + "\n"

  Files.write(fileToModify.toPath, content.getBytes(StandardCharsets.UTF_8))
  vcs.add(fileToModify.getAbsolutePath) !! logger

  state
}
