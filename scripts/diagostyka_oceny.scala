
import scala.util.Random
import scala.io.Source
import java.time.LocalDate
val rand = Random()

def getFnames(sex:Char) = 
  val l = if sex == 'm' then "m" else "k"
  Source.fromFile("nazwiska/imiona.csv").getLines.toList
    .map(s => s.substring(0,1).toUpperCase + s.substring(1).toLowerCase)
    .filter(s => s.split(",").last.trim == l)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => !s.exists(c => c.toInt > 127))

def getSnames(sex:Char) = 
  val namesT = if sex == 'm' then "nazwiska_m.csv" else "nazwiska_z.csv"
  Source.fromFile(s"nazwiska/$namesT").getLines.toList
    .map(s => s.substring(0,1).toUpperCase + s.substring(1).toLowerCase)
    .map(s => s.split(",").head).map(s => s.split(" ").head)
    .filter(s => !s.exists(c => c.toInt > 127))

def getRandomElement(list:List[String]) = 
  val n = rand.nextInt(list.size)
  list(n)

def createNames(N:Int) =
  val fnamesMen = getFnames('m')
  val fnamesWom = getFnames('w')
  val snamesMen = getSnames('m')
  val snamesWom = getSnames('w')
  println(s"fnM = ${fnamesMen.size}, fnW = ${fnamesWom.size}, snM = ${snamesMen.size}, snW = ${snamesWom.size}")
  for i <- 1 to N yield 
    if rand.nextInt(2) < 1 then 
      (i, getRandomElement(fnamesMen), getRandomElement(snamesMen))  
    else
      (i, getRandomElement(fnamesWom), getRandomElement(snamesWom))  

def to2Dig(n:Int) = if n < 10 then s"0$n" else s"$n"

def createDate = 
      val ld = LocalDate.of(LocalDate.now.getYear - 1, 1 + rand.nextInt(12), 1 + rand.nextInt(28))
      s"${ld.getYear}-${to2Dig(ld.getMonthValue)}-${to2Dig(ld.getDayOfMonth)}"

def createMeasure = 
  val pres = rand.nextInt(140) + 60
  val puls = rand.nextInt(20) + 50 + pres/5
  s"${rand.nextInt(205)+1};$createDate;$pres;$puls"

def createMarks(n:Int, student:Int) =
  val nn = n - rand.nextInt(4) + 2
  (1 to nn).map(rand.nextInt(5) + 1).toList

@main
def main():Unit = 
  (1 to 3350).map(i => 
      createMeasure
  ).toList.foreach(println)

