name := "ztmStops"

scalaVersion :="2.12.6"

version := "0.1"

libraryDependencies ++= {
val circeVersion = "0.9.3"
	Seq(
	"io.circe" %% "circe-core" % circeVersion,
        "io.circe" %% "circe-generic" % circeVersion,
        "io.circe" %% "circe-parser" % circeVersion
	)
}
