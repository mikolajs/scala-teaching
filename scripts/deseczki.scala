import scala.util.Random
val r = new Random
val l = 650
def elem = r.nextInt(401) + 10
def createHoles = (1 to 20).map(i => elem)
//var holes = createHoles.sorted.reverse

val theHoles = Map(92->6, 470->3, 228->12, 110->3, 220->3, 100->3, 315->3)
def toHoles =
  val h = for(e <- theHoles) 
    yield (1 to e._2).map(i => e._1).toList
  h.flatten.toList
  
var holes = toHoles.sorted.reverse


def fillSlat = 
  var s = 500
  var part:List[Int] = Nil 
  var i = 0
  holes = holes.filter(h => {
    if h < s then
      part = h::part
      s -= h
      false
    else true
  })
  part.reverse
      	
def greedy() = 
  while holes.size > 0 do
    println("("+fillSlat.mkString(",")+")")

def gcd(a:Int, b:Int):Int = if(b == 0) then a else gcd(b, a%b)

def gcdAll(s:Int) = 
  val holesAndSlat = holes:::List(s)
  holesAndSlat.reduceLeft((a, b) => gcd(b, a))

def dynamicSlat() = 
  //println(holes.size)
  //println(holes.mkString(", "))
  val g = 1 //gcdAll(l)
  var arrOld:Array[List[Int]] = new Array[List[Int]](l/g+1).map(_ => List())
  var arrNew:Array[List[Int]] = new Array[List[Int]](l/g+1).map(_ => List())
  //println("arr size: " + arrOld.size)
  for h <- holes do
    for i <- 1 until arrOld.size do
      if h > i then arrNew(i) = arrOld(i)
      else if h == i then arrNew(i) = List(h)
      else if arrNew(i-1).sum + h <= i then arrNew(i) = arrNew(i-1) ++ List(h)
      else if arrOld(i-h).sum + h >= arrNew(i-1).sum then arrNew(i) = arrOld(i-h) ++ List(h)
      else arrNew(i) = arrNew(i-1)
    val tmp = arrNew
    arrNew = arrOld.map(_ => List())
    arrOld = tmp
  arrOld.last

   
def deleteElem(e:Int) = 
  val i = holes.indexOf(e)
  holes = holes.take(i) ++ holes.drop(i+1)
    

@main def main() = 
  println(holes.sum)
  greedy()
  println("Dynamic algorythm")
  holes = toHoles.sorted
  while holes.size > 0 do 
    val e = dynamicSlat()
    println("(" + e.mkString(",") + ")")
    e.foreach(deleteElem(_))
  //println(toHoles.sorted.reverse)
  ""

/* 
* 33 33 33 50 50 
* 34 34 33 50 50
*/
