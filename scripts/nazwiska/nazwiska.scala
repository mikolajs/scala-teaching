import scala.util.Random
import scala.io.Source
import java.time.LocalDate

val r = Random()

def loadNamesMen = Source.fromFile("nazwiska_m.csv").getLines.toList.map(_.toLowerCase.split('-').head.split(' ').head).filter(n => n(0).toInt < 127)
  .map(n => n(0).toString.toUpperCase + n.drop(1))
  def loadNamesWoman = Source.fromFile("nazwiska_z.csv").getLines.toList.map(_.toLowerCase.split('-').head.split(' ').head).filter(n => n(0).toInt < 127)
  .map(n => n(0).toString.toUpperCase + n.drop(1))
  
def createNames = 
  Random.shuffle(loadNamesMen ++ loadNamesWoman).take(1000)
  
def salary = (r.nextInt(5000) + 4000).toDouble + scala.math.round(100*r.nextDouble())/100.0

@main def run():Unit =
   for name <- createNames do
     println(s"$name $salary")
    
