import scala.util.Random

val r = new Random

var l:Array[Int] = Array.ofDim(50)
var m:List[(Int, Int)] = Nil

def randArray() =
  for i <- 0 until 50 do
    l(i) = r.nextInt(2000000000)+1
  if l.distinct.size < 50 then println("ZA MAŁO!!!!!")


def mkIdol() =
  val idol = r.nextInt(2000000000)+1
  l.foreach(i => m = (i, idol)::m)
  println("size of knowing before: " + m.size)
  while m.size < 200 do
    val a = r.nextInt(50)
    val b = r.nextInt(50)
    if a != b then m = (l(a) -> l(b))::m
    m = m.distinct
    //println(a + " " + b + " " + l(a) + " " + l(b) + " size: " + m.size)


@main def start() =
  randArray()
  //println("size of people " + l.size)
  mkIdol()
  //println("Finish adding knowing" + m.size)
  val f = r.shuffle(m)
  f.map(t => t._1 + " " + t._2).foreach(println)
  ""
