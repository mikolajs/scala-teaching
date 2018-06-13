import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "eu.brosbit",
      scalaVersion := "2.12.6",
      version      := "0.1.0"
    )),
    name := "redis-test",
    libraryDependencies ++= {
	Seq(
	 scalaTest % Test,
	"net.debasishg" %% "redisclient" % "3.7"
        )
  }
)
