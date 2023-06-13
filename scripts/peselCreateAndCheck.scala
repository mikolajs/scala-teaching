import scala.util.Random

val r = Random()

def checkCode(p:String) = 
  val arr = Array(1, 3, 5, 9, 1, 3, 5, 9, 1, 3)
  var sum = 0
  for i <- p.length do
    sum += (p(i).toInt-48)*arr(i)
  sum %= 10
  if sum == 0 then 0
  else 10 - sum

def randomPesel = 
  val p = (r.nextLong(10000000000000)+10000000000).toString.take(10)
  if p.drop(2).take(2).toInt < 33 && p.drop(4).take(2) < 32 then
    p+checkCode(p)
  else ""
  
 @main def main():Unit = 
   println(checkCode("7105220393")
   for i <- 1 to 100 do
     println(randomPesel)
    
 
