import scala.io.Source
import scala.util.Random
import scala.math.*
val rand = Random()

def distances(d:Char) = d match 
  case e if e == 'a' => 100
  case e if e == 'b' => 200
  case e if e == 'c' => 400
  case e if e == 'd' => 800
  case _  => 1000

def randDist = (rand.nextInt(5) + 97).toChar

def mkRandomTri() = 
  val a = rand.nextInt(1000) + 1  
  val b = rand.nextInt(1000) + 1
  val c = if a > b then a - b  + rand.nextInt(b) + 1 else b - a + rand.nextInt(a) + 1
  s"$a $b $c"

@main
def main():Unit = 
  val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.substring(0,1).toUpperCase + s.substring(1).toLowerCase)
    .map(s => s.split(",").head).take(1000).map(s => s.split(" ").head)
    .filter(s => s.head.toInt < 128)
  val lines = Random.shuffle(fnames).take(300)
    .map(s => 
        s"$s ${mkRandomTri()}"
        ).mkString("\n") 
  println(lines)
