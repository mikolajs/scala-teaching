import scala.util.Random
import scala.io.Source
import java.time.LocalDate

val r = Random()

def loadNamesMen = Source.fromFile("nazwiska_m.csv").getLines.toList.map(_.toLowerCase.split('-').head)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1))
def loadNamesWoman = Source.fromFile("nazwiska_z.csv").getLines.toList.map(_.toLowerCase.split('-').head)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1))
def loadFirstNames = 
  val firstNames = Source.fromFile("imiona.csv").getLines.toList.map(_.toLowerCase)
  .map(s => s.substring(0, 1).toUpperCase  + s.substring(1))
  .partition(n => n.last == 'm')
  (firstNames._1.map(_.split(",").head), firstNames._2.map(_.split(",").head))
  
def createMenNames = 
  val names = loadNamesMen
  val firsts = loadFirstNames._1
  (1 to 400).map(i => 
    firsts (r.nextInt(firsts.size)) + ";" + names(r.nextInt(names.size))
  ).toList
  
def createWomanNames = 
  val names = loadNamesWoman
  val firsts = loadFirstNames._2
  (1 to 400).map(i => 
    firsts (r.nextInt(firsts.size)) + ";" + names(r.nextInt(names.size))
  ).toList
  
def createControlPesel(s:String) = 
  val a = Array(1, 3, 7, 9, 1, 3, 7, 9, 1, 3)
  var w = 0
  for i <- 0 until a.size do
    w += (s(i).toInt-'0'.toInt)*a(i)
  w %= 10
  if w == 0 then 0 else 10 - w
  
def createPesel(y:Int) = 
  val ld = LocalDate.of(LocalDate.now.getYear - y + r.nextInt(2), 1 + r.nextInt(12), 1 + r.nextInt(28))
  val a = if ld.getYear % 100 < 10 then "0" + (ld.getYear % 100).toString else (ld.getYear % 100).toString
  val p0 = a + (ld.getMonthValue + 20).toString +  
  ld.toString.split("-").last +
  (1 to 4).map(i => r.nextInt(10).toString).flatten.mkString
  p0 + createControlPesel(p0).toString
  
def createEmail(name:String) = 
  val a = List("_", "-", ".")
  val n = name.split(";").map(s => 
    val k = r.nextInt(s.size)
    s.take(if k <  3 then 3 else k))
  .mkString(a(r.nextInt(3))).toLowerCase
  .filter(ch => ch.toInt < 127) 
  val e = r.nextInt(7)
  e match
    case k if k < 4 => n + "@gmail.com"
    case 4 => n + "@hotmail.com"
    case 5 => n + "@wp.pl"
    case _ => n + "@interia.pl"
  
import scala.util.Random
 //parametr y wiek osoby 
import java.util.Date
val d1 = Date(2008, 0, 1).getTime()
val d2 = Date(2012, 11, 31).getTime()
val deltaT = (d2 - d1)

def birthDay = 
  val d = Date(d1 + r.nextLong(deltaT))
  val m = if d.getMonth()+1 < 10 then s"0${d.getMonth()+1}" else s"${d.getMonth()+1}"
  s"${d.getDate()}.${m}.${d.getYear()}"
  
def getLetter = 
  val n = r.nextInt(52)
  if n < 26 then (n+65).toChar else (n+71).toChar

def getRandLetters = 
  val n = r.nextInt(5) + 1
  (1 to n).map(_ => getLetter).mkString

def getDigits = 
  val n = r.nextInt(5) match 
    case d if d == 4 => r.nextInt(100000) 
    case d if d == 3 => r.nextInt(1000)
    case d if d == 2 => r.nextInt(100)
    case _ => r.nextInt(9) + 1
  val z = r.nextInt(3)
  val zero = (1 to z).map(_ => "0").mkString
  if n > 10000 then n.toString else zero + n.toString

def login = 
   val n = r.nextInt(4) + 2
   (1 to n).map(i => getRandLetters + getDigits).mkString

@main def run(y:Int):Unit =
   val names = createMenNames ++ createWomanNames
  // names.map(s => s + ";" + createEmail(s) +  ";" + createPesel(y).toString + ";").foreach(println)

   val allNames = names.filter(n =>
       n.forall(l => l.toInt < 129)
    ).map(n => 
       n.split(";").mkString(" ")
    )
    val allClasses = r.shuffle(allNames).take(y)
    allClasses.map(name => 
        //val n = r.nextInt(10) + 1
        s"$name ${birthDay} $login"
     ).foreach(println)
    
