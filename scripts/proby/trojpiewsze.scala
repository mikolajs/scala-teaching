import scala.util.boundary.*
import scala.util.boundary

val RANGE = 3000

def isPrime(x:Int):Boolean =
  if x < 2 then false
  else if x == 2 then true
  else if x % 2 == 0 then false
  else
    var returnVar = true
    val s = scala.math.sqrt(x).toInt
    boundary:
      for i <- 3 to s by 2 do
        if x % i == 0 then 
          returnVar = false
          break(1)
    returnVar

def getPrimes:Array[Int] = 
  var primes = List[Int]()
  for i <- 2 to RANGE do
    if isPrime(i) then primes = i :: primes
  primes.reverse.toArray

def getTriprimes(primes:Array[Int]) =  
  val triprimes:Array[Int] = Array.ofDim[Int](RANGE + 1)
  boundary:
    for i <- 0 until primes.length do
      if primes(i)*3 > RANGE then break(0)
      boundary:
        for j <- (i + 1) until primes.length do
          if primes(i)+primes(j)*2 > RANGE then break(0)
          boundary:
            for k <- (j + 1) until primes.length do
              val sum:Int = primes(i) + primes(j) + primes(k)
              if sum > RANGE then break(0)
              if sum <= 3000 then 
                triprimes(sum) += 1
  triprimes

def sieve:Array[Boolean] = 
  val arr = Array.ofDim[Boolean](RANGE+1).map(_ => true)
  arr(0) = false
  arr(1) = false
  for i <- 2 to RANGE do
    if arr(i) then
      var k = i + i
      while k <= RANGE do
        arr(k) = false
        k += i
  arr

def countTriprimes(x:Int, primes:Array[Int], arr:Array[Boolean]):Int = 
  var n = 0
  if arr(x) then
    for i <- 0 until primes.length do
      for j <- (i + 1) until primes.length do
        val d = x - primes(i) - primes(j)
        if d <= RANGE && d > primes(j) && arr(d) then 
         // println(s"${primes(i)}, ${primes(j)}, $d")
          n += 1
    n
  else 0
  

@main def main():Unit = 
  val arr = sieve 
  val primes = (for i <- 2 to RANGE yield
    if arr(i) then i else -1).filter(_ > 0).toArray
  println(s"number of primes: ${primes.length}")
  val triprimes = getTriprimes(primes)
  var maxT = 0
  var v = -1
  for i <- 0 until triprimes.length do
    if arr(i) &&  maxT < triprimes(i) then 
      maxT = triprimes(i)
      v = i
  println(s"nr = $v max = $maxT")
  val s = countTriprimes(v, primes, arr)
  println(s)

