import scala.math
import scala.collection.mutable.ArrayBuffer

def isRectTriangle(a:Int, b:Int) =
  val c = math.floor(math.sqrt(a*a + b*b))
  a*a + b*b == c*c

def findRectTriangles(N:Int) =
  val arrBuff = ArrayBuffer[(Int,Int)]() 
  for a <- 1 to N do
    for b <- 1 to N do
      if isRectTriangle(a, b) then arrBuff += (a.toInt, b.toInt)
  arrBuff

@main def main():Unit =
  val ab = findRectTriangles(10000)
  println(ab.size)


