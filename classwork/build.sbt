val scala3Version = "3.3.7"

lazy val root = project
  .in(file("."))
  .settings(
    name := "classwork",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    
    libraryDependencies += "org.jopendocument" % "jOpenDocument" % "1.3",
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test
  )
