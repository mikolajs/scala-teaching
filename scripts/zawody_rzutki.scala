import scala.util.Random
import scala.io.Source

val rand = Random()

def rzut = rand.nextInt(181)

def seria = (1 to 9).map(i => s"${rzut}").mkString(" ")

def anagram(s1:String, s2:String) =
  if s1.size == s2.size && s1.sorted == s2.sorted then true
  else false


def szukajAnanimow = 
   val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.toUpperCase)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => !s.exists(c => c.toInt > 127))
   for name <- fnames do
      for name2 <- fnames do
          if name2 != name && ananim(name2, name) then
              println(s"$name2 $name")


def szukajAnagramu(s:String) = 
   val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.toUpperCase)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => !s.exists(c => c.toInt > 127))
   for name <- fnames do
      if anagram(name, s) println(name, s)


@main
def main():Unit =
   val fnames = Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.substring(0,1).toUpperCase + s.substring(1).toLowerCase)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => !s.exists(c => c.toInt > 127))
   val lines = Random.shuffle(fnames).take(300)
     .map(n => s"$n $seria").mkString("\n")
   println(lines)

   szukajAnagramu("Marita")
