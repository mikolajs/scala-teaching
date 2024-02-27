lazy val akkaVersion    = "2.9.0"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

lazy val root = (project in file(".")).
  settings(
   assembly / mainClass := Some("eu.brosbit.MainServer"),
    inThisBuild(List(
      organization    := "eu.brosbit",
      scalaVersion    := "2.13.12"
    )),
    name := "akkaMainServer",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "org.scalatest"     %% "scalatest"                % "3.2.12"        % Test
    )
  )
