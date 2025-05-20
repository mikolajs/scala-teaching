import scala.util.Random
import scala.math._

import java.nio.file.{Paths, Files}


def save(file:String, data:String) =
  val p = Paths.get(file)
    Files.write(p, data.getBytes)

val r = new Random()

def randSide = r.nextInt(199)+1

  
def getThird(a:Int, b:Int) = 
  val c = sqrt(a*a + b*b).toInt
  if a*a + b*b == c*c then 
    println("UDANE")
    c
  else 
    val what = r.nextInt(7)
    if what < 6 then randSide
    else 
      (a + b + (r.nextInt(10) + 1))
    

import java.util.Locale

def getIntegral =
  val re = 100*r.nextDouble() - 50.0
  val im = 100*r.nextDouble() - 50.0
  String.format(Locale.US, "%.2f %.2f", re, im)


@main 
def main():Unit =
  val m = (1 to 1000).map( i =>
    val a = randSide
    val b = randSide
    val c = getThird(a, b)
    s"$a $b $c"
    getIntegral
    ).mkString("\n")
  save("/home/ms/Dokumenty/trojkaty.txt", m) 
