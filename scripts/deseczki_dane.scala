import scala.util.Random
val r = new Random
def elem = r.nextInt(401) + 10
val l = r.nextInt(901)+100 
def createHoles = (1 to 20).map(i => elem)
//var holes = createHoles.sorted.reverse

def randomHoles =
  val l = r.nextInt(901)+100 
  val k = r.nextInt(41)+10
  val h = (1 to k).map(i => r.nextInt(l-10)+10).toList
  (l::k::h).mkString(" ")


def fillSlat(holes:List[Int]) = 
  var s = 500
  var part:List[Int] = Nil 
  var i = 0
  holes.filter(h => {
    if h < s then
      part = h::part
      s -= h
      false
    else true
  })
  part.reverse
      	
def greedy(holes:List[Int]) = 
  while holes.size > 0 do
    println("("+fillSlat(holes).mkString(",")+")")

def gcd(a:Int, b:Int):Int = if(b == 0) then a else gcd(b, a%b)

def gcdAll(s:Int, holes:List[Int]) = 
  val holesAndSlat = holes:::List(s)
  holesAndSlat.reduceLeft((a, b) => gcd(b, a))
/*
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
*/
   
def deleteElem(e:Int, holes:List[Int]) = 
  val i = holes.indexOf(e)
  holes.take(i) ++ holes.drop(i+1)
    

@main def main() = 
  (1 to 100).foreach(i => println(randomHoles))
  ""

/* 
* 33 33 33 50 50 
* 34 34 33 50 50
*/
