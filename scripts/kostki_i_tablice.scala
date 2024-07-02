import scala.util.Random

val rand = Random()

def table(N:Int, M:Int):Unit =
  for i <- 1 to N do
    var list:List[Int] = Nil
    for j <- 1 to M do
       val s = rand.nextInt(1700)-700
       list = s::list
    println(list.mkString(" "))

def tableSq(N:Int) = table(N, N)

def smallCube() =
  for _ <- 1 to 20 do
    println(rand.nextInt(701)+100)

@main def main():Unit = 
  //table(10000, 100)
  //smallCube()
  tableSq(501)
  
