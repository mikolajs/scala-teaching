//import org.apache.logging.log4j.core.config.composite.MergeStrategy
import sbt.Keys.mainClass
//import sun.security.tools.PathList

lazy val akkaHttpVersion = "10.2.1"
lazy val akkaVersion    = "2.6.10"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "eu.brosbit",
      scalaVersion    := "2.13.3",
      mainClass in Compile := Some("eu.brosbit.HttpServerRoutingMinimal")
    )),
    name := "GamesOnline",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",
      "org.mongodb.scala" %% "mongo-scala-driver"       % "2.9.0",
      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    )
  )





