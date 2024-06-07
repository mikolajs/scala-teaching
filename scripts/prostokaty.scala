import scala.util.Random

val r = new Random

def randNumb = r.nextInt(20)-10

def noZero = 
  var a = 0
  while a == 0 do
    a = randNumb
  a

def prostokat() =
  val x1 = randNumb
  val y1 = randNumb
  val y2 = y1+r.nextInt(8)+1
  val x2 = x1+r.nextInt(8)+1
  s"$x1 $y1 $x2 $y2 "

def prosta() =
  val a = noZero
  val b = randNumb
  s"$a $b"

@main def start() =
  val m = (1 to 200).map( i =>
   prostokat()+prosta() 
  ).mkString("\n")
  println(m)
  ""

