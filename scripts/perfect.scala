import scala.util.Random

val r = Random()
def mkRand() = 
  val s = r.nextInt(600000) + 1000
  val x = r.nextInt(500000)
  val e = if s + x > 1000000 then 1000000 else s + x
  (s, e)

def mkNearPerfect() = 
  val (s, e) = mkRand()
  val x = r.nextInt(100) + 10
  val d = if x/10000.0 < 0.0001 then 0.0001 else x/10000.0
  (s, e, d)

@main def main():Unit = 
  (1 to 200).map(i => 
    val p = mkRand()
    println(s"${p._1} ${p._2}")
    )
