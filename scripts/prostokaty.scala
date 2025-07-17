import scala.util.Random

val r = Random()

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

def procent():List[Int] = 
  val l = for i <- 1 to 6  yield
    r.nextInt(101)
  l.toList

def countPower(list:List[Int]) = 
  val arr = List(3.6, 1.2, 2.3, 0.7, 1.34, 0.15)
  val s = arr.zip(list).map(t => t._1*t._2).sum
  math.round(s*100)/100.0

@main def start():Unit = 
  (1 to 10000).map(i => 
      val l = procent()
      val p = countPower(l)
      println(s"${l.mkString(" ")} $p")
      )
/*
@main def start() =
  val m = (1 to 200).map( i =>
   prostokat()+prosta() 
  ).mkString("\n")
  println(m)
  ""
*/
