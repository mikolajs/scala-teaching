import java.util.Random

val r = Random()
val N = 10
def rand1 = (1 to (r.nextInt(N)+10)).map(i => (r.nextInt(50)+5, r.nextInt(200)))
def rand = rand1.map(e => (e._1/10, e._1%10 , e._2)).map(e => (s"${e._1}.${e._2}", e._3))
  .map(e => e._1+ " " + e._2)

@main def main():Unit = 
  println(10)
  (1 to 10).foreach(i =>
    val b = rand
    val sum = b.map(s => s.split(' ')(0).toDouble).sum
    println(b.length.toString + " " + (sum/5).toInt)
    println(b.mkString(" "))
    )
