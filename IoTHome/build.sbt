val scala3Version = "3.5.0"
lazy val doobieVersion = "1.0.0-RC5"
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
    name := "weatherSeverVertx",
    version := "0.1.0",
    scalaVersion := scala3Version,
    run / fork := true,
    mainClass := Some("eu.brosbit.Main"),
    assemblyJarName := "weather-assembly-0.1.jar",

    libraryDependencies ++= Seq(
      "io.vertx" % "vertx-core" % "4.5.9",
      "io.vertx" % "vertx-web" % "4.5.9",
      "io.vertx" % "vertx-web-client" % "4.5.9",
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2"   % doobieVersion
    )
  )
