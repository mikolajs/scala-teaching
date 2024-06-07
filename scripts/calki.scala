import scala.util.Random

val r = new Random

def createRandomFunc() = 
  val n = r.nextInt(6) + 3
  val l = (1 to n).map(i => 
    r.nextInt(21) - 10).toList
  n::l 
  
def randSection() = 
  r.nextInt(50).toString + " " + (r.nextInt(50) + 50).toString

@main def start() =
  var p = 1
  var l:List[(Int,Int)] = Nil
  val m = (1 to 200).map( i =>
      createRandomFunc().mkString(" ") + " " +  randSection() 
  ).mkString("\n")
  println(m)
  ""
