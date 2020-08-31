lazy val theVersion    = "0.1"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "eu.brosbit",
      scalaVersion    := "2.13.2"
    )),
    name := "recruitementsScript",
    libraryDependencies ++= Seq(
      "com.github.tototoshi" %% "scala-csv" % "1.3.6"
    )
  )
