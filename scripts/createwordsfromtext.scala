
import scala.io.Source


val text = Source.fromFile("text.txt").getLines.mkString(" ")

val words =  text.split(' ')
  .map(_.trim).filter(_.length > 4).map(_.toLowerCase)
  .map(_.replaceAll("\\.", "")).map(_.replaceAll(",", ""))
  .map(_.trim).filter(_.length == 6)
  .filter(w => w.forall(l => l.toInt > 96 && l.toInt < 123)) 
  .distinct

  words.foreach(println)






