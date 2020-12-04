
import scala.io.Source
import scala.util.Random

val r = new Random()

/*
val buildings1200 = (1 to 1200).toList

val notIn = (1 to 1000).map(_ => (r.nextInt(1201) + 1)).toSet.take(200).toList.sorted

val buildings1000 = buildings1200.filterNot(b => notIn.exists(p => p == b)).take(1000)

def addFloats() = {
  val floats30 = (1 to 30).toList
  val toRemove = (1 to 30).map(_ => (r.nextInt(31) + 1)).toSet.take(10).toList.sorted
  floats30.filterNot(f => toRemove.exists(p => p == f)).take(20)
}

for(b <- buildings1000){
  print(b)
  print(" ")
  val floats = addFloats()
  println(floats.mkString(" "))
}
*/

val sendShipments = (1 to 2000).map{_ => 
 (r.nextInt(1200) + 1, r.nextInt(30) + 1)
}.distinct.take(1000)

sendShipments.sorted.foreach(s => println(s._1 + " " + s._2))



