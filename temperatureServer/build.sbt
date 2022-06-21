val scala3Version = "3.1.2"
cancelable in Global := true

//fork / run := true


ThisBuild / assemblyMergeStrategy := {
 case PathList("META-INF", "vertx", xs @ _*) => MergeStrategy.first
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}

Compile / packageBin / mappings += {
  (baseDirectory.value / "vertx" / "vertx-version.txt") -> "vertx/vertx-version.txt"
}


lazy val root = project
  .in(file("."))
  .settings(
    name := "temperatureServer",
    version := "0.1.0",
    scalaVersion := scala3Version,
    assembly / mainClass := Some("eu.brosbit.Main"),
    assembly / assemblyJarName := "weather-assembly-0.1.jar",

    libraryDependencies ++= Seq(
      "io.vertx" % "vertx-core" % "4.3.1",
      "io.vertx" % "vertx-web" % "4.3.1",
      "io.vertx" % "vertx-web-client" % "4.3.1",
      "org.scalactic" %% "scalactic" % "3.2.12",
      "org.scalatest" %% "scalatest" % "3.2.12" % "test"
    )
  )
