import scala.util.Random
import scala.io.*

val r = Random()

def randLetter = (65+r.nextInt(5)).toChar
def randString(s:Int) =
  for i <- 0 until s do
    print(randLetter)
  println("")
 
def readData = 
  Source.fromFile("podciagi.txt").getLines.toList.take(2)

def findSeries(s1:String, s2:String) =
  val arr = Array.ofDim[String](s1.size+2, s2.size+2)
  for i <- 0 until s1.size
      j <- 0 until s2.size if i != 0 && j != 0 do
      if s1(i) == s2(i) then
        if arr(i-1)(j-1).size > arr(i)(i-1) then arr(i)(j) = arr(i
  
def test1 = 
  val common = findSeries("ABCBCAC", "CBABCDC")
  println(common)

@main def main() = 
  test1
  val s1::s2::Nil = readData
  val common = findSeries(s1, s2)
  println(common)
