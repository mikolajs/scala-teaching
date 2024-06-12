import scala.math
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

val rand = Random()

def isRectTriangle(a:Int, b:Int) =
  val c = math.floor(math.sqrt(a*a + b*b))
  a*a + b*b == c*c

def findRectTriangles(N:Int) =
  val arrBuff = ArrayBuffer[(Int,Int)]() 
  for a <- 1 to N do
    for b <- 1 to N do
      if isRectTriangle(a, b) then arrBuff += ((a.toInt, b.toInt))
  arrBuff

def generateMountains(N:Int, ab:ArrayBuffer[(Int, Int)]) = 
  val s = ab.size
  var X = 0
  var H = 100000
  var points:List[(Int, Int)] = Nil
  points = (X, H)::points
  for i <- 1 to N do
    val (x1, h1) = ab(rand.nextInt(s))
    X += x1
    H += h1
    points = (X, H)::points
    val (x, h) = ab(rand.nextInt(s))
    X += x
    H += h
    points = (X, H)::points
  points





@main def main():Unit =
  val ab = findRectTriangles(10000)
  println(ab.size)
  val mountains = generateMountains(20, ab)
  for m <- mountains do
    println(s"${m._1} ${m._2}")


