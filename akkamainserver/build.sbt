
name := "akkaMainServer"

//organization := "eu.brosbit"

version := "1.0"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

scalaVersion    := "2.13.12"

lazy val akkaVersion    = "2.9.1"

//mainClass in (Compile,run) := Some("eu.brosbit.MainServer")

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor"         % akkaVersion,
      "org.scalatest"     %% "scalatest"                % "3.2.12"        % Test
)
