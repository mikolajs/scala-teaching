import scala.util.Random
import java.nio.file.*

val r = Random()
val o = List('+','*','/')

def createLine =
  val n = r.nextInt(50)+10
  s"${(r.nextInt(50)+50)*n} ${(r.nextInt(50)+50)*n}"

def createFraction = 
  (r.nextInt(20)+1).toString + " " + (r.nextInt(20)+1).toString

def randomOperator = o(r.nextInt(o.length)).toString + " "

def fractionsLine = 
  //val n = r.nextInt(7) + 2
  val n = 2
  var l:List[String] = List()
  for i <- 1 to n do
    l = createFraction::l
  randomOperator + l.mkString(" ")

def toTime(sec:Int) = 
  val s = sec % 60
  val m = (sec / 60) % 60
  val h = (sec / 3600) 
  s"$h $m $s"
  
def timeLine =
  val d = 100
  val t = r.nextInt(8) + 2
  val n = r.nextInt(80)+10
  var l:List[String] = List()
  for i <- 1 to t do
    l = toTime((r.nextInt(d)+d)*n)::l
  t.toString + " " + l.mkString(" ") 

def times =
  val lines = (1 to 500).map(_ => timeLine).toList
  Files.write(Paths.get("zadania_systemu.txt"), lines.mkString("\n").getBytes)

def rooms = 
  val lines = (1 to 500).map(_ => createLine).toList
  Files.write(Paths.get("pokoje.txt"), lines.mkString("\n").getBytes)
  
def fractions = 
  val lines = (1 to 500).map(_ => fractionsLine).toList
  Files.write(Paths.get("ulamki.txt"), lines.mkString("\n").getBytes)

def generators = 
  val l = (1 to 1000).map(_ => (r.nextInt(9000)+1000).toString + " " + (r.nextInt(9000)+1000).toString).toList
  Files.write(Paths.get("generator_losowy.txt"), l.mkString("\n").getBytes)

@main def main:Unit = 
  println(toTime(8285))
  times
  

