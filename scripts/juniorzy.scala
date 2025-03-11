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

def mkRandomRuns(d:Char) = 
  val dist = distances(d)
  val nr = rand.nextInt(21) + 5  
  for i <- 1 to nr yield
    math.round((dist*(0.9 + rand.nextDouble()/2.0) + dist/100.0)*100)/100.0

@main
def main():Unit = 
  val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.substring(0,1).toUpperCase + s.substring(1).toLowerCase)
    .map(s => s.split(",").head).take(500)
    .map(s => 
        val d = randDist
        val runs = mkRandomRuns(d).toList.mkString(" ")
        s"$s $d $runs"
        ).mkString("\n") 
