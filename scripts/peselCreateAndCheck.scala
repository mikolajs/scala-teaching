import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import java.nio.file.{Files, Paths}
import java.time.LocalDate

val r = Random()

def changeLetter(letter:Char) =
  letter match
    case 'Ą' => 'A'
    case 'Ę' => 'E'
    case 'Ó' => 'O'
    case 'Ć' => 'C'
    case 'Ł' => 'L'
    case 'Ń' => 'N'
    case 'Ś' => 'S'
    case 'Ź' | 'Ż' => 'Z'
    case _ => letter

def writeToFile(p:String, data:String) = 
  Files.write(Paths.get(p), data.getBytes)
  

lazy val menNames = Source.fromFile("nazwiska_m.csv").getLines().toList
  .map(_.split(' ').head).map(_.map(changeLetter)).filterNot(n => n.exists(c => c.toInt > 127))
lazy val womanNames = Source.fromFile("nazwiska_z.csv").getLines().toList
  .map(_.split(' ').head).map(_.map(changeLetter)).filterNot(n => n.exists(c => c.toInt > 127))

def checkCode(p:String) = 
  val arr = Array(1, 3, 5, 9, 1, 3, 5, 9, 1, 3)
  var sum = 0
  for i <- 0 until p.length do
    sum += (p(i).toInt-48)*arr(i)
  sum %= 10
  if sum == 0 then 0
  else 10 - sum

def createPesel(y:Int) = 
  val ld = LocalDate.of(LocalDate.now.getYear - y + r.nextInt(2), 1 + r.nextInt(12), 1 + r.nextInt(28))
  val a = if ld.getYear % 100 < 10 then "0" + (ld.getYear % 100).toString else (ld.getYear % 100).toString
  val p0 = a + toMonth(y, ld.getMonthValue) +
       ld.toString.split("-").last +
       (1 to 4).map(i => r.nextInt(10).toString).flatten.mkString
  p0 + checkCode(p0).toString

def toMonth(y:Int, m:Int) = 
  val t = LocalDate.now().getYear - 2000 
  if t <= y && m < 10 then "0"+m.toString
  else if t <= y then m.toString
  else (m+20).toString

def randomPesel = createPesel(r.nextInt(70)+5) 

def mens(pesel:String):Boolean = pesel(9).toInt % 2 == 1
  
def addName(pesel:String) = 
  if mens(pesel) then  menNames(r.nextInt(menNames.length))
  else womanNames(r.nextInt(womanNames.length))

@main def main():Unit = 
  val ab = ArrayBuffer[String]()  
  while ab.length < 250000 do
    val pesel = randomPesel
    if pesel.length == 11 then ab += pesel+ " " +addName(pesel)
    else println(pesel)
  
  writeToFile("poszukiwanie.txt", ab.mkString("\n"))
  writeToFile("lista_uzytkownikow.txt", ab.sorted.mkString("\n"))

 
