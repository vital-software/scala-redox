import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
import scalariform.formatter.preferences._

organization := "com.github.vital-software"

name := "scala-redox"

scalaVersion := "2.12.6"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
)

val playJsonVersion = "2.6.9"
val playVersion = "2.6.17"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % playJsonVersion,
  "com.typesafe.play" %% "play-json-joda" % playJsonVersion,
  "com.typesafe.play" %% "play-ahc-ws" % playVersion,
  "com.typesafe.play" %% "play-ws-standalone-json" % "1.1.2",
  "com.typesafe.akka" %% "akka-http" % "10.0.14",

  "com.github.vital-software" %% "json-annotation" % "0.6.0",
  "com.github.nscala-time" %% "nscala-time" % "2.14.0",

  "com.typesafe.play" %% "play-specs2" % playVersion % Test,
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation")

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

Test / publishArtifact := false

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
Compile / scalariformFormat / sourceDirectories := (Compile / unmanagedSourceDirectories).value

// PGP settings
pgpPassphrase := Some(Array())
usePgpKeyHex("1bfe664d074b29f8")

// Release settings
releaseTagName              := s"${if (releaseUseGlobalVersion.value) (ThisBuild / version).value else version.value}" // Remove v prefix
releaseTagComment           := s"Releasing ${(ThisBuild / version).value}\n\n[skip ci]"
releaseCommitMessage        := s"Setting version to ${(ThisBuild / version).value}\n\n[skip ci]"

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  setReleaseVersion,
  updateLines,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

releasePublishArtifactsAction := PgpKeys.publishSigned.value

// Test settings
Test / testOptions ++= Seq(
  Tests.Argument("junitxml")
)

updateLinesSchema := Seq(
  UpdateLine(
    file("README.md"),
    _.contains("""libraryDependencies += "com.github.vital-software" %% "scala-redox" % """),
    (v, _) => s"""libraryDependencies += "com.github.vital-software" %% "scala-redox" % "$v""""
  ),
  UpdateLine(
    file("CHANGELOG.md"),
    _.contains("## [Unreleased]"),
    (v, _) => s"## [Unreleased]\n\n## [$v] - ${java.time.LocalDate.now}"
  )
)
