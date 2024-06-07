import scala.util.Random

val r = new Random

def toSysString(nn:Int, s:Int) =
  var str = ""
  var n = nn
  while(n > 0) do
   str = s"${if n%s < 10 then (n%s+48).toChar else ((n % s) + 55).toChar}$str"
   n /= s
  end while 
  str

def mkRandomOperation() = 
  val o = r.nextInt(4)
  o match 
   case 0 => '+'
   case 1 => '-'
   case 2 => '*'
   case 3 => '%'

def mkRandomNumber() = 
  val s = r.nextInt(31)+2
  val n = r.nextInt(1000)+10
  val str = toSysString(n, s) 
  s"$str $s"


@main def start() =
  var p = 1
  val m = (1 to 101).map( i =>
    if i % 2 == 0 then  mkRandomOperation() else mkRandomNumber()
  ).mkString("\n")
  println(m)
  ""
