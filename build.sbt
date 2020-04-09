import com.typesafe.sbt.SbtGit.GitKeys._

name := "random-data-generator-magnolia"
version := "2.9-SNAPSHOT"

scalacOptions in Test ++= Seq("-Yrangepos", "-language:higherKinds")

libraryDependencies ++= {
  if (scalaVersion.value.startsWith("2.10."))
    Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))
  else Seq()
}

libraryDependencies ++= {
  val Magnolia = "0.14.2"
  val Scalacheck = "1.14.3"
  val Spec2 = "4.6.0"

  Seq(
    "com.propensive" %% "magnolia" % Magnolia,
    "org.scalacheck" %% "scalacheck" % Scalacheck,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.specs2" %% "specs2-core" % Spec2 % "test",
    "org.specs2" %% "specs2-mock" % Spec2 % "test"
  )
}

lazy val standardSettings = Seq(
  name := "random-data-generator-magnolia",
  scalaVersion := "2.13.0",
  crossScalaVersions := Seq("2.13.0", "2.12.6"),
  organization := "com.danielasfregola",
  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
  homepage := Some(url("https://github.com/DanielaSfregola/random-data-generator-magnolia")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/DanielaSfregola/random-data-generator-magnolia"),
            "scm:git:git@github.com:DanielaSfregola/random-data-generator-magnolia.git")),
  apiURL := None, // TBD scaladoc needed?
  pomExtra := (
    <developers>
    <developer>
      <id>DanielaSfregola</id>
      <name>Daniela Sfregola</name>
      <url>http://danielasfregola.com/</url>
    </developer>
  </developers>
  ),
  publishMavenStyle := true,
  publishTo := {
    if (version.value.trim.endsWith("SNAPSHOT")) Some(Opts.resolver.sonatypeSnapshots)
    else Some(Opts.resolver.sonatypeStaging)
  },
  gitRemoteRepo := "git@github.com:DanielaSfregola/random-data-generator-magnolia.git",
  scalacOptions ++= Seq("-encoding",
                        "UTF-8",
                        "-deprecation",
                        "-Xfatal-warnings",
                        "-feature",
                        "-unchecked",
                        "-language:postfixOps",
                        "-language:experimental.macros"),
  scalacOptions in (Compile, doc) ++= Seq("-sourcepath", baseDirectory.value.getAbsolutePath),
  autoAPIMappings := true,
  apiURL := None,
  scalacOptions in (Compile, doc) ++= {
    val branch = if (version.value.trim.endsWith("SNAPSHOT")) "master" else version.value
    Seq(
      "-doc-source-url",
      "https://github.com/DanielaSfregola/random-data-generator-magnolia/tree/" + branch + "€{FILE_PATH}.scala"
    )
  }
)

lazy val root = (project in file("."))
  .settings(standardSettings)
