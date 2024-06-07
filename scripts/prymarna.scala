import scala.util.Random
import java.nio.file.{Files, Paths}

val r = Random()

val pierwsze = 
  (for i <- 2 to 1000 yield 
    if pierwsza(i) then i
    else -1).filter(_ > 0).take(100)

def pierwsza(n:Int):Boolean =
  if n == 2 then return true
  else if n % 2 == 0 then return false
  else
    var i = 3
    while i * i <= n do
      if n % i == 0 then return false
      i += 2
  return true

def naPrymarna(n:Int):String =
  val arr = Array.ofDim[Int](100)
  var N = n
  var i = 2
  var j = 0
  for p <- pierwsze do
    while N % p == 0 do
      arr(j) += 1
      N /= p
    j += 1
  arr.reverse.dropWhile(_ == 0).map(e => 
      if e < 10 then (e+48).toChar
      else (e+87).toChar)
  .reverse.mkString

def losujLiczby(n:Int) =
  var num = 550
  var ile = 0
  var liczby:List[String] = Nil
  while ile < n do
    num += r.nextInt(10000)
    if !pierwsza(n) then
      val p = naPrymarna(num)
      if p.nonEmpty then 
        liczby = p::liczby
        ile += 1
  liczby
    

@main def main():Unit =
   val liczby = losujLiczby(1000)
   val path = Paths.get("prymarne.txt")
   Files.write(path, liczby.mkString("\n").getBytes)

