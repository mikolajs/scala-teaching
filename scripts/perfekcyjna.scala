import scala.util.Random
import scala.math.*

val rand = Random()

def pierwsza(n:Int):Boolean =
  if n == 2 then return true
  else if n % 2 == 0 then return false
  else
    var i = 3
    while i * i <= n do
      if n % i == 0 then return false
      i += 2
  return true

val pierwsze =
 (for i <- 2 to 100000 yield
   if pierwsza(i) then i else -1).filter(_ > 0).take(10000).toList

def mkPerfect(p:Int) = 
  val m = (log(1000000000000000)/log(p)).toInt
  val r = rand.nextInt(m-1)+2
  println(pow(p,r).toLong)

@main def main() =
  val p = pierwsze
  //println(p.size)
  for i <- 1 to 99 do
    val r = rand.nextInt(12000)
    if r < p.size then
      mkPerfect(pierwsze(r))
    else 
      println(rand.nextLong(1000000000000000L))
