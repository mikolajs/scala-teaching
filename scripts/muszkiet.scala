import scala.math.*

@main def main(d:Int, speed:Int):Unit =
  val r = d.toDouble / 2.0
  val V = 4.0/3.0*Pi*r*r*r/1000.0
  val g = 11.3
  val m = g*V
  println(s"Waga: $m")
  println(s"Energia: ${m*speed*speed/2.0}")


