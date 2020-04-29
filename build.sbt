import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

organization := "com.github.vital-software"

name := "scala-redox"

scalaVersion := "2.12.8"

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
  "com.typesafe.play" %% "play-json-joda" % playJsonVersion,
  "com.typesafe.play" %% "play-ahc-ws" % playVersion,
  "com.typesafe.play" %% "play-ws-standalone-json" % "1.1.2",
  "com.typesafe.akka" %% "akka-http" % "10.1.8",
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
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

Test / publishArtifact := false

pomIncludeRepository := { _ =>
  false
}

sonatypeProfileName := "com.github.vital-software"

pomExtra := (<url>https://github.com/vital-software/scala-redox</url>
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

// GPG settings
credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  "B9513278AF9A10374E07A88FAA24C7523BD70F36",
  "ignored"
)

// Release settings
releaseTagName := s"${if (releaseUseGlobalVersion.value) (ThisBuild / version).value else version.value}" // Remove v prefix
releaseTagComment := s"Releasing ${(ThisBuild / version).value}\n\n[skip ci]"
releaseCommitMessage := s"Setting version to ${(ThisBuild / version).value}\n\n[skip ci]"

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  setReleaseVersion,
  updateLines,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

// Test settings
Test / testOptions ++= Seq(
  Tests.Argument("junitxml")
)

val unreleasedCompare = """^\[Unreleased\]: https://github\.com/(.*)/compare/(.*)\.\.\.HEAD$""".r
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
  ),
  UpdateLine(
    file("CHANGELOG.md"),
    unreleasedCompare.unapplySeq(_).isDefined,
    (v, compareLine) =>
      compareLine match {
        case unreleasedCompare(project, previous) =>
          s"[Unreleased]: https://github.com/$project/compare/$v...HEAD\n[$v]: https://github.com/$project/compare/$previous...$v"
      }
  ),
)


mimaPreviousArtifacts := Set(organization.value %% name.value % "8.0.1")

mimaBinaryIssueFilters ++= Seq(
  // e.g. ProblemFilters.exclude[MissingClassProblem]("co.vitaler.events.*"),
)
