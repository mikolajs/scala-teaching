import scala.util.Random
import java.nio.file.{Files, Paths}
val r = new Random


def createChar(n:Int) = 
  if n < 10 then (n + '0'.toInt).toChar
  else if n < 36 then (n - 10 + 'A'.toInt).toChar
  else (n -36 + 'a'.toInt).toChar

def mkText = 
  val N = r.nextInt(500)+100
  val s = for i <- 1 to N yield
    val n = r.nextInt(62)
    createChar(n)
  (s.mkString) +" "+ (r.nextInt(97)+2).toString


@main def start():Unit = 
  val data = (1 to 200).map(i => mkText).mkString("\n")
  val path = Paths.get("flawiusz_napisy.txt")
  Files.write(path, data.getBytes)
  
