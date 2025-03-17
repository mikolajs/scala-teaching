import scala.util.Random
import scala.io.Source

val rand = Random()

def rzut = rand.nextInt(181)

def seria = (1 to 9).map(i => s"${rzut}").mkString(" ")

@main
def main():Unit =
   val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.substring(0,1).toUpperCase + s.substring(1).toLowerCase)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => s.head.toInt < 128)
   val lines = Random.shuffle(fnames).take(300)
     .map(n => s"$n $seria").mkString("\n")
   println(lines)

  
