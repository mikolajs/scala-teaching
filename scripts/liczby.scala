import scala.util.Random

val r = Random()


@main def main():Unit =
  for i <- 1 to 10000 do
    println(r.nextInt(10000000))
