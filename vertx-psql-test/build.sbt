val scala3Version = "3.5.0"
lazy val doobieVersion = "1.0.0-RC5"
cancelable in Global := true
ThisBuild / organization := "eu.brosbit"
ThisBuild / scalaVersion := "3.5.0"
//fork / run := true


ThisBuild / assemblyMergeStrategy := {
 case PathList("META-INF", "vertx", xs @ _*) => MergeStrategy.first
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}

Compile / packageBin / mappings += {
  (baseDirectory.value / "vertx" / "vertx-version.txt") -> "vertx/vertx-version.txt"
}

val ver = "0.5"

lazy val root = project
  .in(file("."))
  .settings(
    name := "iothome-server",
    version := ver,
    scalaVersion := scala3Version,
    run / fork := true,
   // mainClass := Some("eu.brosbit.Main"),
    assemblyJarName := s"iothome-assembly-$ver.jar",
    assembly / mainClass := Some("eu.brosbit.Main"),
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies ++= Seq(
      "io.vertx" % "vertx-core" % "4.5.9",
      "io.vertx" % "vertx-web" % "4.5.9",
      "io.vertx" % "vertx-web-client" % "4.5.9",
      "io.vertx"% "vertx-pg-client"% "4.5.9",
      "com.ongres.scram" % "client" % "2.1",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "ch.qos.logback" % "logback-classic" % "1.3.5",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
