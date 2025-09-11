import scala.util.Random

val r = new Random()

val words = (1 to 100).map(_ => {
  val n = r.nextInt(5) + 4
  var s =  (r.nextInt(9) + 66).toChar.toString
  for(_ <- 2 to n) s += (r.nextInt(10) + 65).toChar.toString
  s})

@main def main():Unit =
  words.foreach(println)
