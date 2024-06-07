import scala.util.Random

val r = Random
var node = 1
var nodes:List[Int] = List(1) 
val stackSize = 10
val leftSize = (1 until 5).foldLeft(2)((x,y) => x*2)
var str = ""

def ifCreateNewNode(line:Int) = 
  val rand = r.nextDouble()*(20.0/(6+line))
  rand > 0.1 

def count(i:Int) = i.toString + (0 until (3-i.toString.length)).map(x => " ").mkString

def printLine = 
  val s = nodes.map(l => count(l)).mkString(" ")
  val p = leftSize*2 - s.length/2
  val b = (0 until p).map(x => " ").mkString
  println(b+s+b)

def printTreeStart = 
  printLine
  for i <- 1 to stackSize do
    var newNodes:List[Int]= Nil
    for e <- nodes do
      if e != 0 then 
        for x <- 1 to 2 do
          if ifCreateNewNode(i) then
            node += 1
            newNodes = node::newNodes
            str += s"$e $node\n"
          else newNodes = 0::newNodes
      else    
        newNodes = 0::newNodes
        newNodes = 0::newNodes
    nodes = newNodes.reverse
    //printLine
    
def generateAntsPlace(antsNum:Int) = 
  var ants:Array[Boolean] = Array.ofDim[Boolean](node).map(x => false) 
  var len = 0
  while len < antsNum do
    val a = r.nextInt(node)
    if !ants(a) then
      ants(a) = true
      len += 1
  val antsList = for i <- 0 until ants.length if ants(i) 
    yield i+1
  antsList.mkString("\n")
    
def generateRandomPlace(numb:Int) =
  var places:List[Int] = Nil
  for i <- 1 to numb do
   val p = r.nextInt(node)+1
   places = p::places
  places.mkString("\n")

@main def run():Unit = 
  val path = java.nio.file.Paths.get("komiwojadzer.txt")
  printTreeStart
  str = s"$node\n" + str
  val antsNum = node/8 + r.nextInt(node/6) 
  str += s"$antsNum\n"
  str += generateRandomPlace(antsNum)
  str += "\n"
  java.nio.file.Files.write(path, str.getBytes)
  

