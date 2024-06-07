import scala.util.Random
import java.nio.file.{Files, Paths}
val r = new Random


//def mkRandom = r.nextLong(200000000000L)-100000000000L
def mkDouble = Math.round(r.nextDouble()*60000)/10000.0
def mkInt = r.nextInt(1000000) + 1000000000
def point = s"$mkDouble $mkDouble"

def kubelkowe =
  (1 to 100000).map(_ => mkInt).mkString("\n")

def randomChar = (r.nextInt(10) + 'A'.toInt).toChar

def ciagi = 
  val m1 = (1 to 100).map(i => randomChar).mkString
  val m2 = (1 to 100).map(i => randomChar).mkString
  val m3 = (1 to 3000).map(i => randomChar).mkString
  val m4 = (1 to 3000).map(i => randomChar).mkString

  val path = Paths.get("ciagi.txt")
  Files.write(path, (s"$m1\n$m2\n$m3\n$m4").getBytes)

@main def start():Unit = 
  val data = (1 to 20000).map(i => point).mkString("\n")
  val path = Paths.get("kubelkowe.txt")
  Files.write(path, kubelkowe.getBytes)
  
