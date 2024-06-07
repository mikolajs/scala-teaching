import scala.util.Random
import java.nio.file.{Files, Paths}

val r = Random()

val pierwsze = 
  (for i <- 2 to 100000000 yield 
    if pierwsza(i) then i
    else -1).filter(_ > 0)

def pierwsza(n:Int):Boolean =
  if n == 2 then return true
  else if n % 2 == 0 then return false
  else
    var i = 3
    while i * i <= n do
      if n % i == 0 then return false
      i += 2
  return true


def losujLiczby(n:Int) =
  var primary = pierwsze.toArray 
  var liczby:List[(Long, Long)] = Nil
  val s = primary.size
  for _ <- 1 to n do
    val result = r.nextInt(10000)+1
    val b = primary(r.nextInt(s)).toLong*result*2
    val a = primary(r.nextInt(s)).toLong*result+b
    liczby = (a,b)::liczby
  liczby
    

@main def main():Unit =
   val liczby = losujLiczby(50000)
   val path = Paths.get("wspolne_reszty.txt")
   Files.write(path, liczby.map(x => s"${x._1} ${x._2}").mkString("\n").getBytes)

