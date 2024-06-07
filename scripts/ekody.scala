import scala.util.Random

val r = new Random

def genLetter:Char = ('A'.toInt + r.nextInt(26)).toChar
def randCodes =
  val letters = genLetter.toString+genLetter.toString 
  val numFirst = r.nextInt(100000).toString
  val numMiddle = r.nextInt(Math.pow(9,r.nextInt(5)+1).toInt).toString match {
    case "0" => ""
    case x => x }
  val numLast = r.nextInt(100).toString match {
    case "0" => ""
    case x => x }
  letters + numFirst + numMiddle + " " + numMiddle + numLast
  

@main def start():Unit =
  val m = (1 to 200).map( i => randCodes).mkString("\n")
  println(m)
  
