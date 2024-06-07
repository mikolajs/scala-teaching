import scala.util.Random

val r = new Random

def randAngle() = r.nextInt(80) +  20 

def toPoints(R:Int, deg:Int) = 
   val radians = 2*Math.PI*deg/360.0
   (Math.round(R*Math.sin(radians)).toInt, Math.round(R*Math.cos(radians)).toInt)

def createPolygon():List[(Int, Int)] = 
  val radius:Int = r.nextInt(10)+10
  var t = (0,0)
  var angleNow:Int = 0
  var l:List[(Int,Int)] = Nil
  while true do
     angleNow += randAngle()
     if angleNow > 360 then return l
     else 
       t = toPoints(radius, angleNow)
       l = t::l
  l

def toCheckPoint = s" ${r.nextInt(20) - 10} ${r.nextInt(20) - 10}"

@main def start() =
  var l:List[(Int,Int)] = Nil
  val m = (1 to 200).map( i =>
    val l = createPolygon().reverse 
    l.size.toString() + " " + 
     l.map(t => t._1 + " " + t._2).mkString(" ") + toCheckPoint
  ).mkString("\n")
  println(m)
  ""
