import scala.util.Random

val rand = Random()

def genWord = 
  val s = rand.nextInt(41)+10
  (1 to s).map(i => (rand.nextInt(26)+97).toChar).mkString
  
@main
def main():Unit =
  val toAnanims = (1 to 3).map(_ => (rand.nextInt(300), rand.nextInt(300))).toList
  println(toAnanims)
  var data = (1 to 300).map(_ => genWord).toArray
  for t <- toAnanims do
    val a = t._1
    val b = t._2
    if a == b then println("Error")
    data(a) = data(b).reverse
  println(data.mkString("\n"))

