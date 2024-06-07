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


def mkLetter() = (r.nextInt(26)+97).toChar
def mkBigLetter() = (r.nextInt(26)+65).toChar

def mkRandomBigString() = 
  val l = r.nextInt(50)+10
  (1 to l).map(i => mkBigLetter()).mkString

def mkRandomString() = 
  val l = r.nextInt(9)+2
  (1 to l).map(i => mkLetter()).mkString

@main def start():Unit =
  val m = (1 to 400).map( i =>
    mkRandomBigString()
  ).mkString("\n")
  println(m)


