import scala.util.Random
import scala.collection.mutable.Queue
import scala.collection.mutable.Set

val rand = Random()

val letters:Array[Char] = 
  Random.shuffle(
    for i <- 0 to 51 yield
      if i <= 25 then (i+65).toChar else (i + 71).toChar
  ).toArray

var nr = 0

class HTree(g:String): 
  val gen = g
  var list:List[HTree] = Nil

var tree = HTree(letters.head.toString)     
val queue:Queue[HTree] = Queue()
var unique:Set[String] = Set()

def mkTree(ht:HTree) = 
  var a = rand.nextInt(2)  
  var g = rand.nextInt(3) 
  for i <- 1 to a do
    if nr < letters.size then 
      val genDup = ht.gen + ht.gen.last.toString()//(rand.nextInt(ht.gen.length))
      if !unique.contains(genDup) then
        val newHT = HTree(genDup)
        ht.list = newHT::ht.list
        queue += newHT
        unique += genDup
  for i <- 1 to g do
    if nr < letters.size then 
      val genMut = ht.gen + letters(nr).toString()
      if !unique.contains(genMut) then
        nr += 1
        val newHT = HTree(genMut)
        ht.list = newHT::ht.list
        queue += newHT
        unique += genMut  


def createTree:Unit = 
  queue += tree
  while !queue.isEmpty do
    val ht = queue.dequeue()
    mkTree(ht)

def printTree:Unit =
  queue += tree
  var listCodes:List[String] = Nil 
  while !queue.isEmpty do
    val ht = queue.dequeue()
    listCodes = s"${Random.shuffle(ht.gen)}"::listCodes
    for innerHT <- ht.list do 
      queue += innerHT
  println(s"""${listCodes.length} ${Random.shuffle(listCodes).mkString(" ")}""")


@main def main():Unit =
  createTree
  printTree
