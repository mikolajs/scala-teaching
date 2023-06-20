import scala.util.Random
import scala.io.Source
import java.nio.file.{Files, Paths}

val r = Random()

def changeLetter(letter:Char) =
  letter match
  case 'ą' => 'a'
  case 'ę' => 'e'
  case 'ó' => 'o'
  case 'ć' => 'c'
  case 'ł' => 'l'
  case 'ń' => 'n'
  case 'ś' => 's'
  case 'ź' | 'ż' => 'z'
  case _ => letter


def addNumb(s:String) = 
  val n = r.nextInt(10)
  val p = r.nextInt(s.length)
  s.take(p) + n.toString + s.drop(p)

def addUpper(s:String) =
  val p = r.nextInt(s.length)
  s.take(p) + s(p).toString.toUpperCase()+ s.drop(p+1)

def addAt(s:String) = 
  val p = r.nextInt(s.length)
  s.take(p) + '@'.toString + s.drop(p)

def toExcalmation(s:String) = 
  ///TODO change 1 or i or I to !
  s
  
def addUnderscore(s:String) = 
  val p = r.nextInt(s.length)
  s.take(p) + '_'.toString + s.drop(p)

def randomize(s:String) = 
  var str = s
  for i <- 0 until (r.nextInt(2) + 1) do str = addNumb(str)
  for i <- 0 until (r.nextInt(4) + 1) do str = addUpper(str)
  if r.nextInt(2) == 1 then str = addUnderscore(str) 
  if r.nextInt(3) == 1 then str = addAt(str)
  str

val symb = "!@#$%^&*()"

def randomPass:String = 
  val s = r.nextInt(5)+8
  val ps = for i <- 0 to s yield
    val k = r.nextInt(16)
    k match
      case x if x < 3 => (r.nextInt(10)+48).toChar
      case x if x < 5 => symb(r.nextInt(symb.length))
      case x if x < 8 => (r.nextInt(26)+65).toChar
      case _  => (r.nextInt(26)+97).toChar
  ps.mkString

def writeToFile(p:String, data:String) = 
  Files.write(Paths.get(p), data.getBytes)

@main def main():Unit = 
  val po = 30 
  val names = Source.fromFile("imiona.csv").getLines().toList 
   .map(_.split(','))
   .map(_.head).map(_.toLowerCase())
   .map(n => n.map(changeLetter(_)))
   .filterNot(n => n.exists(c => c < 33 || c > 127))
   .map(n => (for i <- 0 to (r.nextInt(po)+1) yield n).toList)
   .flatten
   .map(randomize(_))
   .distinct.take(200000)

  val users = names.sorted.map(n => n + " " + randomPass)
  writeToFile("loginy.txt", users.mkString("\n"))
  writeToFile("logowania.txt",Random.shuffle(users).mkString("\n"))
  println(users.size)
