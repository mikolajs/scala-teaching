import scala.util.Random

val r = new Random

def randFactor() = myRound(r.nextDouble()*20-10)

def createLine() = 
  val n = r.nextInt(10)+2
  var a  = n.toString + " "
  for i <- 0 to n do
    a += randFactor() + " "
  a += randFactor()
  a

def myRound(d:Double) = Math.round(d*10000).toDouble/10000.0

@main def start() =
  val m = (1 to 200).map( i =>
    createLine()
  ).mkString("\n")
  println(m)
  ""
