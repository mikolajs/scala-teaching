import scala.util.Random

val r = Random()

val N = 100000

@main def main():Unit = 
  (1 to N).foreach( i =>
    println(s"${r.nextInt(100)+5} ${r.nextInt(100)+1}")
    )
