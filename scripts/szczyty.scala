import scala.math
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

val rand = Random()
val MAX = 1000
def generatePoint:String = 
  val x = rand.nextInt(MAX*2) - MAX
  val y = rand.nextInt(MAX-1)+1
  s"$x $y"


@main def main():Unit =
  for m <- 1 to 201 do
    println(generatePoint)


