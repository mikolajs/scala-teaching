import scala.util.Random

val rand = Random()

def table(N:Int, M:Int):Unit =
  for i <- 1 to N do
    var list:List[Int] = Nil
    for j <- 1 to M do
       val s = rand.nextInt(190)-90
       list = s::list
    println(list.mkString(" "))

def tableSq(N:Int) = table(N, N)

@main def main():Unit = 
  table(200, 400)
  
