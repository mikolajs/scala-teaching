import scala.util.Random

val r = Random()

val slowa = "tedy jestem zupelnie spokojny zwlaszcza ze posiadasz wprawdzie duzy majatek ale nie jestes tak bogaty jak pallas albo seneka bo widzisz u nas teraz dobrze jest pisac wiersze spiewac przy lutni deklamowac i scigac sie w cyrku ale jeszcze lepiej a zwlaszcza bezpieczniej jest nie pisywac wierszy nie grac nie spiewac i nie scigac sie w cyrku Najlepiej zas jest umiec podziwiac gdy to czyni miedzianobrody jestes pieknym chlopcem wiec ci to chyba moze grozic ze poppea zakocha sie w tobie ale ona zbyt na to doswiadczona milosci zazyla dosc przy dwoch pierwszych mezach a przy trzecim chodzi jej o co innego czy wiesz ze ten glupi otho kocha ja dotad do szalenstwa chodzi tam po skalach hiszpanii i wzdycha tak zas stracil dawne przyzwyczajenia i tak przestal dbac o siebie ze na ukladanie fryzury wystarczy mu teraz trzy godziny dziennie kto by sie tego spodziewal zwlaszcza po othonie"

def create(s:String, N:Int):String = 
  val e = r.nextInt(50)
  if s.size > N then s
  else 
    e match
      case x if x < 15 => mkExclamation(s, N)
      case x if x < 30 => mkQuotation(s, N)
      case x if x < 38 => mkGreeter(s, N)
      case x if x < 45 => mkLower(s, N)
      case _ => mkDiamond(s, N)

def getLetterPos(s:String) = 
  println("all pos: " +s)
  val l = for i <- 0 until s.size yield if s(i).toInt >= 97 then i else -1
  l.filter(_ > -1).toList

def getNextLetter(ch:Char) = 
  val n = ch.toInt+1
  if n > 122 then 'a' else n.toChar
  
def getPreviousLetter(ch:Char) = 
  val n = ch.toInt-1
  if n < 97 then 'z' else n.toChar

def getGreeterLetter(ch:Char) = 
  val l = r.nextInt(123 - ch.toInt)
  val n = ch.toInt+l
  if n > 122 then 'z' else n.toChar

def getLowerLetter(ch:Char) = 
  val l = r.nextInt(ch.toInt - 96)
  val n = ch.toInt-l
  if n < 97 then 'a' else n.toChar
  
def mkExclamation(s:String, N:Int) = 
  var l:List[Int] = getLetterPos(s)
  val p = r.nextInt(l.size)
  val f = s(l(p))
  val ch = getLowerLetter(f)
  println("drawn: " + f + " add: " + ch)
  val str = if r.nextInt(2) == 0 then s"${s.take(l(p))}$f$ch!${s.drop(l(p)+1)}"
  else s"${s.take(l(p))}$ch$f!${s.drop(l(p)+1)}"
  if f == ch then create(s, N) else create(str, N)
  
def mkQuotation(s:String, N:Int) = 
  var l:List[Int] = getLetterPos(s)
  val p = r.nextInt(l.size)
  val f = s(l(p))
  val ch = getGreeterLetter(f)
  println("drawn: " + f + " add: " + ch)
  val str = if r.nextInt(2) == 0 then s"${s.take(l(p))}$f$ch?${s.drop(l(p)+1)}"
  else s"${s.take(l(p))}$ch$f?${s.drop(l(p)+1)}"
  if f == ch then create(s, N) else create(str, N)
  
def mkGreeter(s:String, N:Int) = 
  var l:List[Int] = getLetterPos(s)
  val p = r.nextInt(l.size)
  val f = s(l(p))
  val ch = getNextLetter(f)
  println("drawn: " + f + " change to: " + ch)
  create(s"${s.take(l(p))}$ch<${s.drop(l(p)+1)}", N)

def mkLower(s:String, N:Int) = 
  var l:List[Int] = getLetterPos(s)
  val p = r.nextInt(l.size)
  val f = s(l(p))
  val ch = getPreviousLetter(f)
  println("drawn: " + f + " change to: " + ch)
  create(s"${s.take(l(p))}$ch>${s.drop(l(p)+1)}", N)

def mkDiamond(s:String, N:Int) = 
  create(s, N)


@main def main():Unit = 
  val list = slowa.split(' ').map(_.trim)
    .map(line => 
     if line.size == 1 then line
     else create(line,  2*line.size+line.size/3))
  list.foreach(println)
  //println(create("abcdefghijklmnoprstuwxyz", 40))
  
  
