val scala3Version = "3.5.0"
lazy val doobieVersion = "1.0.0-RC5"

lazy val root = project
  .in(file("."))
  .settings(
    name := "serverData",
    version := "0.1.0-SNAPSHOT",
    run / fork := true,
    scalaVersion := scala3Version,


    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2"   % doobieVersion,
      "org.scalameta" %% "munit" % "1.0.0" % Test
    )
  )
