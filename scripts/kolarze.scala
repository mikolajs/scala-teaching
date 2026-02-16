
import scala.util.Random
import scala.io.Source

val r = Random()

def loadNamesMen = Source.fromFile("nazwiska/nazwiska_m.csv").getLines.toList.map(_.toLowerCase.split('-').head)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1))
def loadFirstNames =
  val firstNames = Source.fromFile("nazwiska/imiona.csv").getLines.toList.map(_.toLowerCase)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1))
  .partition(n => n.last == 'm')
  (firstNames._1.map(_.split(",").head), firstNames._2.map(_.split(",").head))

def createMenNames(numb:Int) =
  val names = loadNamesMen
  val firsts = loadFirstNames._1
  (1 to numb).map(i =>
    firsts (r.nextInt(firsts.size)) + ";" + names(r.nextInt(names.size))
  ).toList


// rekord na 200m ze startu lotnego 8,941
def randDouble = 8.9 + r.nextDouble()

def trenig10runs = 
  val line = (1 to 10).map(i => scala.math.round(randDouble*1000)/1000.0).mkString(";")
  line.replace(".", ",")

@main
def main():Unit = 
  val men = createMenNames(2000)
  for m <- men do
    println(s"$m;$trenig10runs")
