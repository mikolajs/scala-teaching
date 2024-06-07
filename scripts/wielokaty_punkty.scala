import scala.util.Random

val r = new Random

def randPoint(s:Int) = (r.nextInt(12) + 5, r.nextInt(20) + s)

def getUpper():List[(Int,Int)] = 
  var t = (0,0)
  var l:List[(Int,Int)] = Nil
  while true do
    t = randPoint(20)
    if l.size != 0 && l.head._1 + t._1 > 40 then return l
    else if l.size == 0 then l = t::l
    else l = (t._1 + l.head._1, t._2)::l
  l
  
def getBottom() = getUpper().map(t => (t._1, t._2 - 20))

def createPolygon() = 
  getUpper().reverse ++ getBottom()

def toCheckPoint = s" ${r.nextInt(40) - 20} ${r.nextInt(40) - 20}"

@main def start() =
  var l:List[(Int,Int)] = Nil
  val m = (1 to 200).map( i =>
    l = createPolygon()
    l.size.toString() + " " + l.map(t => (t._1 - 20, t._2 - 20))
      .map(t => t._1 + " " + t._2).mkString(" ") + toCheckPoint
  ).mkString("\n")
  println(m)
  ""
