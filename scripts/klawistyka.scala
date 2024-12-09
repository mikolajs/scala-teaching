import scala.util.Random
import scala.collection.mutable.Queue

val rand = Random()

val letters:Array[Char] = 
  Random.shuffle(
    for i <- 0 to 51 yield
      if i <= 25 then (i+65).toChar else (i + 71).toChar
  ).toArray

var nr = 0
class HTree(gen:String) =
  var list:List[HTree] = Nil

var tree = HTree(letters.head.toString)     

def mkTree(ht:HTree) = 
  var a = rand.nextInt(3)
  var g = rand.nextInt(3)
  for i <- 1 to a do
     val genDup = ht.gen + ht.gen(rand.nextInt(ht.gen.length)
     val newHT = HTree(genDup)
     ht.list = newHT::ht.list
  for i <- 1 to g do
    if nr < letters.size 
      val genMut = ht.gen + letters(nr)
      nr += 1
      val newHT = HTree(genMut)
      ht.list = newHT::ht.list





@main def main():Unit =
  for ch <- letters do
    println(ch)

