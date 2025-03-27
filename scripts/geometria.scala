import scala.util.Random
import scala.math.round

val rand = Random()

def createLine = 
  val x1 = round((rand.nextDouble()*4.0-2.0)*100.0)/100.0
  val x3 = round((x1 + rand.nextDouble()*2.0)*100.0)/100.0
  val x2 = if x3 == x1 then x1 + 1.0 else x3
  val dxt = rand.nextDouble()/200.0
  val dxx = if dxt < 0.0001 then 0.0001 else dxt
  val dx = "%.4f".formatLocal(java.util.Locale.US, dxx)
  s"$x1 $x2 $dx"
@main
def main():Unit = 
  val data = (1 to 200).map(i => createLine).mkString("\n")
  print(data)
