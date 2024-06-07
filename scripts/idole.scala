import scala.util.Random

val r = new Random

def createLine() = 
  val n = r.nextInt(99)+1
  val idol = r.nextInt(1000000000)+1
  var l:List[Int] = Nil
  for i <- 1 to n do
    if r.nextInt(2) == 0 then l = (r.nextInt(1000000000)+1)::l
    else l = idol::l
  n::l

@main def start() =
  val m = (1 to 200).map( i =>
    createLine().mkString(" ")
  ).mkString("\n")
  println(m)
  ""
