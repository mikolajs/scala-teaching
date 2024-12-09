import scala.util.Random
import scala.collection.mutable.Queue

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

def mkTree(ht:HTree) = 
  var a = rand.nextInt(3)
  var g = rand.nextInt(2)
  for i <- 1 to a do
    if nr < letters.size then 
      val genDup = ht.gen + ht.gen(rand.nextInt(ht.gen.length))
      val newHT = HTree(genDup)
      ht.list = newHT::ht.list
      queue += newHT
  for i <- 1 to g do
    if nr < letters.size then 
      val genMut = ht.gen + letters(nr).toString()
      nr += 1
      val newHT = HTree(genMut)
      ht.list = newHT::ht.list
      queue += newHT


def createTree:Unit = 
  queue += tree
  while !queue.isEmpty do
    val ht = queue.dequeue()
    mkTree(ht)

def printTree:Unit =
  queue += tree
  var str = ""
  while !queue.isEmpty do
    val ht = queue.dequeue()
    println(s"${ht.gen}")
    for innerHT <- ht.list do 
      queue += innerHT
  //println(str)


@main def main():Unit =
  createTree
  printTree
