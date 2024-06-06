import scala.util.Random

val r = Random()

def createString() = 
  var n = r.nextInt(1000000)+1
  var str = ""
  while n > 0 do
    if n > 1000 && r.nextInt(10) > 3 then
      val a = 1000*(r.nextInt(26)+1)
      val l = if a > n then (n/1000)*1000 else a
      str += (64+l/1000).toChar
      n -= l
    else 
      val a = r.nextInt(26)+1
      val l = if a > n then n else a
      str += (96+l).toChar
      n -= l
  str



@main def main():Unit = 
  (1 to 500).map(i =>
    println(createString())
    )
