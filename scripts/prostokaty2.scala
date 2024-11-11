import scala.util.Random

val rand = Random()

def rectangel = s"${rand.nextInt(37)+3} ${rand.nextInt(37)+3}"

@main def main():Unit =
  (1 to 200).foreach(i =>
      println(rectangel)
    )
