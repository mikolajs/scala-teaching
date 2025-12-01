import scala.util.Random

val rand = Random()


def code = 
  (1 to 3).map(i => rand.between(65,75).toChar).mkString + rand.between(1000, 1000000)

@main def main():Unit =
  for i <- 1 to 1000 do
    println(code)
