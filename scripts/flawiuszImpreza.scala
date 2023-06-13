import scala.util.Random
import scala.io.Source
import java.time.LocalDate
import java.nio.file.{Files, Paths}

val r = Random()

def toEnglish(s:String) =
  s.map { ch =>
    ch match
      case 'ą' => 'a'
      case 'ę' => 'e'
      case 'ó' => 'o'
      case 'ć' => 'c'
      case 'ł' => 'l'
      case 'ń' => 'n'
      case 'ś' => 's'
      case 'ź' => 'z'
      case 'ż' => 'z'
      case l => l 
   }

def loadFirstNames = 
  val firstNames = Source.fromFile("imiona.csv").getLines.toList.map(_.toLowerCase)
  .map(toEnglish(_)).map(s => s.substring(0, 1).toUpperCase  + s.substring(1))
  firstNames.map(_.split(",").head).map(_.split(" ").head).map(_.trim).filter(_.nonEmpty).filter(str => !str.exists(c => c.toInt > 123))
  
  
  
 //parametr y wiek osoby 
@main def main():Unit =
  var str = "46\n"
  for i <- 1 to 46 do
    val n = r.nextInt(9000)+1000
    val names = loadFirstNames 
    str += n.toString
    str += " "
    str += r.nextInt(99)+1
    str += " "
    str += r.nextInt(98)+2
    str += "\n"
    str += r.shuffle(names).take(n).mkString(" ")
    str += "\n"
  val path = Paths.get("imprezy_taneczne.txt")
  Files.write(path, str.getBytes)
   



