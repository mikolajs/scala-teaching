    name := "Akka primes"
     
    version := "0.1"
     
    scalaVersion := "2.12.1"
     
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
    
    scalacOptions ++= Seq("-deprecation", "-unchecked")
 
    libraryDependencies ++= {
    Seq(
	    "com.typesafe.akka" % "akka-actor_2.12" % "2.5.0",
    "org.scalatest" % "scalatest_2.12"  % "3.0.3" 
    )}


