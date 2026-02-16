
import scala.util.Random
import scala.io.Source

val r = Random()

def loadNamesMen = Source.fromFile("nazwiska_m.csv").getLines.toList.map(_.toLowerCase.split('-').head)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1))
def loadNamesWoman = Source.fromFile("nazwiska_z.csv").getLines.toList.map(_.toLowerCase.split('-').head)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1))
def loadFirstNames =
  val firstNames = Source.fromFile("imiona.csv").getLines.toList.map(_.toLowerCase)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1))
  .partition(n => n.last == 'm')
  (firstNames._1.map(_.split(",").head), firstNames._2.map(_.split(",").head))

def createMenNames(numb:Int) =
  val names = loadNamesMen
  val firsts = loadFirstNames._1
  (1 to numb).map(i =>
    firsts (r.nextInt(firsts.size)) + ";" + names(r.nextInt(names.size))
  ).toList

def createWomanNames(numb:Int) =
  val names = loadNamesWoman
  val firsts = loadFirstNames._2
  (1 to numb).map(i =>
    firsts (r.nextInt(firsts.size)) + ";" + names(r.nextInt(names.size))
  ).toList


@main
def main():Unit = 
  val men = createMenNames(2000)
  for m <- men do
    println(s"$m;")
