
import java.util.Date
import scala.util.Random


val r = Random()

def insertData(T:Float, Ta:Float, ta:Long, Tb:Float, tb:Long):Unit =
  val boilerTime = Date().getTime
  DBConnect.mkInsertBoiler(boilerTime, T)
  if Ta > 0.0f then
    DBConnect.mkInsertMeasure('A', ta, Ta)
    DBConnect.mkInsertMeasureUsed('A', ta, boilerTime)
  if Tb > 0.0f then
    DBConnect.mkInsertMeasure('B', tb, Tb)
    DBConnect.mkInsertMeasureUsed('B', tb, boilerTime)

@main def main():Unit =
  for i <- 1 to 20 do
    val T = r.nextFloat*30.0f
    Thread.sleep(r.nextLong(123))
    val ta = new Date().getTime
    val a = TemperatureData.add('A', ta, T)
    Thread.sleep(r.nextLong(123))
    val tb = new Date().getTime
    val b = TemperatureData.add('B', tb, T + 0.4f)
    println(s"$i Temperature of rooms $T")
    if a > 0.0f then println(s"A. Set boiler to: $a")
    if b > 0.0f then println(s"B. Set boiler to: $b")
    val TBoiler = if a > 0.0f then a else b
    insertData(TBoiler, T,ta, T+0.4f, tb)
    Thread.sleep(10000)