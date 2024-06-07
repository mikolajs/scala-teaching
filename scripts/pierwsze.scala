import scala.util.Random

val r = Random()

def pierwsza(n:Int):Boolean = 
  if n == 2 then return true
  else if n % 2 == 0 then return false
  else 
    var i = 3
    while i * i <= n do
      if n % i == 0 then return false
      i += 2
  return true

def zakres =
  val s = r.nextInt(10000)+2
  s"$s ${s+100+r.nextInt(10000)}"

@main def start():Unit = 
  //var i = s 
  //while true do  
  //  if pierwsza(i) then 
  //    println(i)
  //  i += 1
  val list = 
  for i <- 2 to 1000 yield 
    if pierwsza(i) then i
    else -1
  val listaPierwszych = list.filter(_ > 0).take(100)
  println(listaPierwszych.size)
  println(listaPierwszych.mkString(" "))

