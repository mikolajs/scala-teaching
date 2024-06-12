
import scala.util.Random
import scala.io.Source

def allLetters() = (for i <- 65 to 90 yield i.toChar).mkString

def randomLetters(letters:String) = Random.shuffle(letters)

def readText() = 
  val lines = Source.fromFile("tekst.txt").getLines.toList.head.split(' ')
  var words:List[String] = Nil
  for line <- lines do
    if line.size > 7 then words = line::words
  words



@main def main():Unit = 
   val s = allLetters()
   val words = readText().take(30)
   for w <- words do
     println(s"${w} ${randomLetters(s)}")
