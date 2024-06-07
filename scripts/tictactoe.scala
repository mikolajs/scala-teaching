import scala.util.Random

val r = new Random

def rand = 
  val n = r.nextInt(3)
  n match
    case 0 => '.'
    case 1 => 'X'
    case _ => 'O'


def createMatrix = 
  val ar = Array.ofDim[Char](3, 3)
  for i <- 0 until 3
    j <- 0 until 3 do
    ar(i)(j) = rand
  ar


@main def start():Unit =
  (1 to 100).foreach(i =>
    println(createMatrix.map(_.mkString).mkString("\n"))
  )


