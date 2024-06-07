import scala.util.Random

val r = new Random

def randNumb = r.nextInt(20)-10

def line() =
  val a = randNumb
  val b = randNumb 
  val x1 = randNumb
  val x2 = randNumb 
  val y1 = randNumb 
  val y2 = randNumb 
  val X = randNumb
  var Y = 0
  if r.nextInt(3) == 0 then
    Y = a*X + b
  else
    Y = randNumb
  s"$x1 $y1 $x2 $y2 $X $Y"

@main def start() =
  val m = (1 to 100).map( i =>
    line()  
  ).mkString("\n")
  println(m)
  ""
