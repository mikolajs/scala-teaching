
name := "Primes viewer"
 
version := "0.1"
 
scalaVersion := "2.11.7"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
Seq(
"com.typesafe" % "config" % "1.3.0",   
"com.typesafe.akka" % "akka-actor_2.11" % "2.4.1",
"org.scalafx" %% "scalafx" % "8.0.60-R9",
"org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"
)} 
 
unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))

    
    
    

