import Dependencies._

ThisBuild / organization := "eu.brosbit"
ThisBuild / version := "0.0.1"
ThisBuild / scalaVersion      := "3.4.2"
ThisBuild / fork              := true
ThisBuild / scalacOptions     := optionsOnOrElse("2.13", "2.12")("-Ywarn-unused")("").value
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / scalafixDependencies ++= List("com.github.liancheng" %% "organize-imports" % "0.6.0")

def settingsApp = Seq(
  name := "homeServerZio",
  Compile / run / mainClass := Option("eu.brosbit.homeserverzio.MainApp"),
  testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  libraryDependencies ++= Seq(
    zioHttp,
    zioTest.cross(CrossVersion.for3Use2_13),
    zioTestSBT.cross(CrossVersion.for3Use2_13),
    zioTestMagnolia.cross(CrossVersion.for3Use2_13),
    zioMySQL
  ),
)

def settingsDocker = Seq(
  Docker / version          := version.value,
  dockerBaseImage := "eclipse-temurin:20.0.1_9-jre",
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(settingsApp)
  .settings(settingsDocker)

addCommandAlias("fmt", "scalafmt; Test / scalafmt; sFix;")
addCommandAlias("fmtCheck", "scalafmtCheck; Test / scalafmtCheck; sFixCheck")
addCommandAlias("sFix", "scalafix OrganizeImports; Test / scalafix OrganizeImports")
addCommandAlias("sFixCheck", "scalafix --check OrganizeImports; Test / scalafix --check OrganizeImports")
