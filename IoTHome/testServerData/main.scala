
import java.util.Date
import scala.util.Random
val r = Random()
case class TemperatureMeasure(t:Long, T:Float)



object TemperatureData:
  val MAX_SIZE = 20
  val queueT = scala.collection.mutable.Queue[TemperatureMeasure]()
  def add(t:Long, T:Float) = 
    queueT += TemperatureMeasure(t, T)
    if queueT.size > MAX_SIZE then
      val tm = queueT.dequeue
      println(tm)

@main def main():Unit =
  for _ <- 1 to 100 do
    TemperatureData.add(new Date().getTime, r.nextFloat*25.0f)      

