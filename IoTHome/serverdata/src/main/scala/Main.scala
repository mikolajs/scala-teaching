
import java.util.Date
import scala.util.Random


val r = Random()


@main def main():Unit =
  for i <- 1 to 100 do
    val t = r.nextFloat*30.0f
    val a = TemperatureData.add('A', new Date().getTime, t)
    val b = TemperatureData.add('B', new Date().getTime, t + 0.4f)
    println(s"$i Temperature of rooms $t")
    if a > 0.0f then println(s"A. Set boiler to: $a")
    if b > 0.0f then println(s"B. Set boiler to: $b")
    Thread.sleep(10000)