import scala.util.Random
import scala.io.Source

val rand = Random()

def mecz = 1.0 + rand.nextInt(14)/2.0

def seria = (1 to (rand.nextInt(6)+5)).map(i => s"${mecz}").mkString(" ")

def anagram(s1:String, s2:String) =
  if s1.size == s2.size && s1.sorted == s2.sorted then true
  else false


def szukajAnagramow = 
   val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.toUpperCase)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => !s.exists(c => c.toInt > 127))
   for name <- fnames do
      for name2 <- fnames do
          if name2 != name && anagram(name2, name) then
              println(s"$name2 $name")

def szukajAnagramow(names:List[String]):Unit = 
   val unames = names.map(_.toUpperCase)
   for name <- unames do
      for name2 <- unames do
          if name2 != name && anagram(name2, name) then
              println(s"$name2 $name")
          //else if name2 == name then println("powtarza się " + name2) 

def szukajAnagramu(s:String) = 
   val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.toUpperCase)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => !s.exists(c => c.toInt > 127))
   for name <- fnames do
      if anagram(name, s) then println(s"$name, $s")


@main
def main():Unit =
   val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.substring(0,1).toUpperCase + s.substring(1).toLowerCase)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => !s.exists(c => c.toInt > 127))
   val linesList = Random.shuffle(fnames).take(300)
   val lines  = linesList.map(n => s"$n $seria").mkString("\n")
   println(lines)

   szukajAnagramow(linesList)
