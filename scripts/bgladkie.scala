import scala.util.Random
import java.nio.file.{Files, Paths}
val r = new Random


//def mkRandom = r.nextLong(200000000000L)-100000000000L
def mkDouble = Math.round(r.nextDouble()*60000)/10000.0
def point = s"$mkDouble $mkDouble"

def randomChar = (r.nextInt(10) + 'A'.toInt).toChar

var primes = List(2)

def isPrime(n:Int):Boolean = 
  for p <- primes do
    if p*p > n  then return true
    if n % p == 0 then return false
  return true

def primesList =
  for i <- 3 to 1000 by 2 do 
    if isPrime(i) then primes = primes :+ i  

def factorial(n:Int):List[Int] = 
  var dividers: List[Int] = Nil
  var N = n;
  var i = 2
  for p <- primes do
    while N % p == 0 do
      dividers = p::dividers
      N /= p
    if N == 1 then return dividers
  return dividers
      

def toPrimeSystem(n:Int) = 
   val dividers = factorial(n).sorted
   val arr = Array.ofDim[Int](primes.size)
   var last = 0
   var i = -1 
   for p <- primes do 
     if last != p then 
       i += 1

   dividers.foreach(d =>
     if last != d then
       i += 1
       last = d
     arr(i) += 1
   )
   if arr.exists(d => d > 9) then "0"
   else 
      arr.reverse.dropWhile(a => a == 0).reverse.mkString

@main def start():Unit =
  primesList
  for p <- primes do
    print(p + " ")
  println()
  val perfects = for i <- 2 to 1000 yield (i, toPrimeSystem(i))
  perfects.foreach(t => println(t._1 + " " + t._2)
  //val data = (1 to 20000).map(i => point).mkString("\n")
  //val path = Paths.get("bgladkie1.txt")
  //Files.write(path, data.getBytes)

