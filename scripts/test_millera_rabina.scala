import scala.util.Random
import java.nio.file.{Files, Paths}
import scala.math.*

val r = Random()
val howManyPrimes = 10000000

def power2Fun = 
  var p = 1L
  (1 to 63).map(_ => 
    val x = p
    p *= 2
    x
    ).toList

val powers2 = power2Fun.reverse

def findPower2(n:Long):(Long, Int) = 
  var s = 63
  for p <- powers2 do
    if n % p == 0 then return (p, s)
    s -= 1
  return (1, 0)

def isPrime(n:Int):Boolean = 
  if n < 2 then return false
  else if n == 2 then return true
  else if n % 2 == 0 then return false
  for i <- 3 until n by 2 do
    if i*i > n then return true
    if n % i == 0 then return false
  return true

def findPrimes = 
  var p:List[Int] = Nil
  for i <- 2 to howManyPrimes do 
    if isPrime(i) then p = i::p
  p.reverse

val primes = findPrimes

def isPrimeWithList(n:Long):Boolean =
  for p <- primes do
    if p*p > n then return true
    if n % p == 0 then return false
  return true

//wrong: TODO: need change a and w to BigIntiger
def modN(n:Long, a0:Long, p0:Long) = 
  var w = BigInt("1")
  var a = BigInt(a0)
  var p = p0
  while p > 1 do
    if p % 2 == 1 then 
      w = (w*a) % n
    a *= a
    a %= n
    p /= 2
  a*w % n
  
def mkTest(n:Long, p:Long, s:Int):Boolean = 
  val n1 = n - 1
  val d = n1 / p
  if d*p != n1 then println("Wrong d and p (2^s))")
  println(s"start test for $n $p $s")
  val k = 40
  for i <- 1 to k do
    val a = r.nextLong(n-2)+1
    val mod0 = modN(n, a, d)
    if mod0 == 1 || mod0 == n1 then return false
    for j <- 1 to s - 1 do
      if modN(n, a, math.pow(2, j).toLong*d) != n1 then return false
  return true

def generateMillerRabinPrime():Long = 
  var n = r.nextLong(1000000000)
  if n < 0 then n *= -1
  if n < 1000000 then n += 1000000
  if n % 2 == 0 then n -= 1
  while true do
    val (p, s) = findPower2(n-1)
    if p > 8 && mkTest(n, p, s) then return n
    else n -= 2
  return 2 
    
def getMillerRabinPrimes(n:Int):List[Long] =
  (1 to n).map(_ => generateMillerRabinPrime()).toList


def generateRandomPrime():Long = 
  var n = r.nextLong(100L*howManyPrimes.toLong)
  if n < 0 then n *= -1
  if n < 1000000 then n += 1000000
  if n % 2 == 0 then n -= 1
  while true do
    if isPrimeWithList(n) then return n 
    else n -= 2
  return 2

def generateSureListOfPrimes(n:Int) = 
  (1 to n).map(_ => generateRandomPrime()).toList

def perfekcyjna() = 
  var a = r.nextLong(100000000000000L)
  var n = r.nextInt(46)
  var k = pow(a, 1.0/n.toDouble).toInt
  while !primes.contains(k) do 
    a = r.nextLong(100000000000000L)
    n = r.nextInt(46)
    k = pow(a, 1.0/n.toDouble).toInt
  pow(k, n).toLong 

def modulo_number() = 
  val rp = generateRandomPrime()
  var sn = r.nextLong(rp)
  if sn % 2 == 1 then sn -= 1
  s"$rp $sn\n"
    

@main def main():Unit = 
  println(s"prime numbers found ${primes.size}")
  val i = 300
  //var list:List[Long] = generateSureListOfPrimes(20,primes)
  var list:List[Long] = Nil
  /*while list.size < 3000 do
    list = list++generateSureListOfPrimes(1000,primes)
    list = list.filter(p => p > 1000000 && p < 2100000000)
    println(s"size of sure generated lists ${list.size}")
    
  while list.size < 10000 do
    list = r.nextLong(2100000000)::list
    
  */
  //var list:List[Long] = getMillerRabinPrimes(20)
  //while i > 0 do 
  //   val p = r.nextInt(2100000000)
  //   if p % 2 == 0 && p > 2 then
  //    list = p::list
  //     i -= 1
  /* var z = 0
  for e <- list do
    if isPrimeWithList(e, primes) then z += 1
  println("primes draw: "+ z )
 */
  
  var t = 0
  while t < 50000 do
    val per = perfekcyjna()
    if per > 100 then 
      list = per::list
      t += 1
      //println(per)    

  list = list.distinct
  println(s"perfect numbers are ${list.size}") 
  while list.size < 5000 do
    list = (r.nextLong(100000000000000L))::list
 
  
  list = r.shuffle(list)
  val adata = list.mkString("\n")
  val path = Paths.get("liczby_perfekcyjne.txt")
  Files.write(path, adata.getBytes)

