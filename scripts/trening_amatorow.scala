import scala.util.Random
import scala.io.Source
import java.util.Locale

val rand = Random()

def okrazenie = String.format(Locale.US, "%.2f", (200.0 + 100.0*rand.nextDouble()))

def seria = (1 to 4).map(i => s"${okrazenie}").mkString(" ")

def snameLetter = (65+rand.nextInt(26)).toChar

@main
def main():Unit =
   val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.substring(0,1).toUpperCase + s.substring(1).toLowerCase)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => !s.exists(c => c.toInt > 127))
   val linesList = Random.shuffle(fnames.take(500).distinct).take(300)
   val lines  = linesList.map(n => s"$n ${snameLetter} $seria").mkString("\n")
   println(lines)

