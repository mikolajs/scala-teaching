
// give the user a nice default project!
ThisBuild / organization := "eu.brosbit"
ThisBuild / version := "0.1"

val ZioVersion   = "2.0.1"
val ZHTTPVersion = "2.0.0-RC11"


lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(BuildHelper.stdSettings)
  .settings(
    name := "Auth0-ZioHttp",
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    libraryDependencies ++= Seq(
      "dev.zio"       %% "zio"            % ZioVersion,
      "dev.zio"       %% "zio-json"       % "0.4.2",
      "io.d11"        %% "zhttp"          % ZHTTPVersion,
      "io.d11" %% "zhttp" % ZHTTPVersion % Test,
      //"dev.zio" %% "zio-test"     % ZioVersion % Test,
      //"dev.zio" %% "zio-test-sbt" % ZioVersion % Test,
      "org.scalactic" %% "scalactic" % "3.2.10",
      "org.scalatest" %% "scalatest" % "3.2.10" % "test"
    ),
  )
  .settings(
    Docker / version          := version.value,
    Compile / run / mainClass := Option("eu.brosbit.MainApp"),
  )

addCommandAlias("fmt", "scalafmt; Test / scalafmt; sFix;")
addCommandAlias("fmtCheck", "scalafmtCheck; Test / scalafmtCheck; sFixCheck")
addCommandAlias("sFix", "scalafix OrganizeImports; Test / scalafix OrganizeImports")
addCommandAlias("sFixCheck", "scalafix --check OrganizeImports; Test / scalafix --check OrganizeImports")

