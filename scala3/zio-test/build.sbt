

ThisBuild / organization := "dev.zio"
ThisBuild / version := "0.0.1"
ThisBuild / scalaVersion := "3.2.1"
ThisBuild / homepage := Some(url("https://github.com/mikolajs"))
ThisBuild / description := "test"
ThisBuild / licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / developers := List(
  Developer(
    "johnDoe",
    "John Doe",
    "@johndoe",
    url("https://github.com/johndoe")
  )
)


val ZioVersion = "2.0.6"

import sbt._


lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(BuildHelper.stdSettings)
  .settings(
    assembly / mainClass := Some("eu.brosbit.MainApp"),
    assembly / assemblyJarName := "zio-test.jar",
    name := "Zio-Test",
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    libraryDependencies ++= Seq(
      "dev.zio"       %% "zio"            % ZioVersion,
      "dev.zio"       %% "zio-json"       % "0.4.2",
      "dev.zio" %% "zio-test"     % ZioVersion % Test,
      "dev.zio" %% "zio-test-sbt" % ZioVersion % Test,
    ),
  )
  .settings(
    Compile / run / mainClass := Option("eu.brosbit.MainApp"),
  )

addCommandAlias("fmt", "scalafmt; Test / scalafmt; sFix;")
addCommandAlias("fmtCheck", "scalafmtCheck; Test / scalafmtCheck; sFixCheck")
addCommandAlias("sFix", "scalafix OrganizeImports; Test / scalafix OrganizeImports")
addCommandAlias("sFixCheck", "scalafix --check OrganizeImports; Test / scalafix --check OrganizeImports")
