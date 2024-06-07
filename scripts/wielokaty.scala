import scala.util.Random

val r = new Random

def randPoint(s:Int) = (r.nextInt(90) + 20, r.nextInt(100) + s)

def getUpper():List[(Int,Int)] = 
  var t = (0,0)
  var l:List[(Int,Int)] = Nil
  while true do
    t = randPoint(100)
    if l.size != 0 && l.head._1 + t._1 > 300 then return l
    else if l.size == 0 then l = t::l
    else l = (t._1 + l.head._1, t._2)::l
  l
  
def getBottom() = getUpper().map(t => (t._1, t._2 - 100))

def createPolygon() = 
  getUpper().reverse ++ getBottom()

@main def start() =
  var p = 1
  var l:List[(Int,Int)] = Nil
  val m = (1 to 100).map( i =>
    l = createPolygon()
    l.size.toString() + " " + l.map(t => t._1 + " " + t._2).mkString(" ")
  ).mkString("\n")
  println(m)
  ""
