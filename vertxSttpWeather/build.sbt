val scala3Version = "3.1.0"
cancelable in Global := true

//fork / run := true


lazy val root = project
  .in(file("."))
  .settings(
    name := "weatherSeverVertx",
    version := "0.1.0",
    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "io.vertx" % "vertx-core" % "4.3.1",
      "io.vertx" % "vertx-web" % "4.3.1",
      "io.vertx" % "vertx-web-client" % "4.3.1",
      "com.softwaremill.sttp.client3" %% "core" % "3.6.2",
      "org.scalactic" %% "scalactic" % "3.2.12",
      "org.scalatest" %% "scalatest" % "3.2.12" % "test"
    )
  )
