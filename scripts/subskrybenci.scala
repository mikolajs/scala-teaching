import scala.io.Source
import scala.util.Random
import scala.math.*

val rand = Random()
def loadNamesMen = Source.fromFile("nazwiska/nazwiska_m.csv").getLines.map(_.toLowerCase.split('-').head)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1)).toArray
def loadNamesWoman = Source.fromFile("nazwiska/nazwiska_z.csv").getLines.map(_.toLowerCase.split('-').head)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1)).toArray

@main
def main():Unit =
  val menArray = loadNamesMen
  val womanArray = loadNamesWoman
  val names = (1 to 900).map(i =>
      if rand.nextInt(2) == 0 then
        val r = rand.nextInt(menArray.size)
        menArray(r)
      else
        val r = rand.nextInt(womanArray.size)
        womanArray(r)
      ).distinct.take(500)
  for name <- names do
     val mc = rand.nextInt(18)+4
     val money = math.round(100.0*(mc*10.0 + rand.nextDouble()*mc*30))/100.0
     println(s"$name $money $mc")
