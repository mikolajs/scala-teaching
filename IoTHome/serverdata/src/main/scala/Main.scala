
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

def mkRoundTemp(T: Float): Float = math.round(T * 100) / 100.0f

@main def main():Unit =
  var tempStart = 16.0f
  for i <- 1 to 30 do
    val TA = tempStart + (r.nextFloat*2.0f - 1.0f)
    val TB= tempStart + (r.nextFloat*2.0f - 1.0f)
    Thread.sleep(r.nextLong(123))
    TemperatureData.addMeasure('A', Date().getTime, TA)
    Thread.sleep(r.nextLong(123))
    TemperatureData.addMeasure('B', Date().getTime, TB)
    print(s"$i Temperature of rooms A -> $TA B -> $TB")
    val T  = TemperatureData.getTemperatureForBoiler
    println(s" set boiler temp $T")
    tempStart += 0.3f
    if i > 20 then tempStart -= 0.8f
    Thread.sleep(2000)

  val boilerSet = DBConnect.mkSelectBoilerSet(20)
  for bs <- boilerSet do
    println(s"date: ${Date(bs.t).toString}, temp: ${mkRoundTemp(bs.T)}")


