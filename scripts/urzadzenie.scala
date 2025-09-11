import scala.util.Random
import scala.math.*

val r = new Random

def line() =
  val p1 = r.nextInt(101)
  val p2 = r.nextInt(101)
  val p3 = r.nextInt(101)
  val p4 = r.nextInt(101)
  val p5 = r.nextInt(101)
  val p6 = r.nextInt(101)
  val P = p1*0.2 + p2*5.9 + p3*2.1 + p4*3.15 + p5*0.82 + p6*7.02
  val PN = if r.nextInt(2) == 0 then
    P*(1+r.nextDouble())
  else P*r.nextDouble()
  val T = if PN >= P then "TAK" else "NIE"
  s"$p1 $p1 $p3 $p4 $p5 $p6 ${round(PN*100)/100.0} $T"


@main def start() =
  val m = (1 to 5000).map( i =>
      line()
  ).mkString("\n")
  println(m)

