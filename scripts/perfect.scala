import scala.util.Random
import scala.math

val r = Random()
def mkRand() = 
  val s = r.nextInt(200000) + 1000
  val x = r.nextInt(300000)
  val e = if s + x > 400000 then 400000 else s + x
  (s, e)

def mkNearPerfect() = 
  val (s, e) = mkRand()
  val x = r.nextInt(500)+ 10
  val d = x / 10000.0

  (s, e, d)

@main def main():Unit = 
  (1 to 200).map(i => 
    val p = mkNearPerfect()
    println(s"${p._1} ${p._2} ${p._3}")
    )
