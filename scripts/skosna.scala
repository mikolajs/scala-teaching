import scala.util.Random

val r = Random()

val s2_asc = (0 to 30).map(i => (Math.pow(2, i+1)-1).toInt)
val s2 = s2_asc.reverse.toArray
//s2.foreach(println)

def toScrew(n:Int) = 
  var N = n
  var screw:List[Int] = Nil
  s2.foreach(w => 
    screw = (N / w)::screw
    N %= w
  )
  screw.reverse.dropWhile(_ == 0).map(_.toString).mkString


def fromScrew(str:String) =
  var n = 0
  val screw = str.reverse.toList.map(_.toInt - '0')
  s2_asc.zip(screw).foreach(t =>
    n += t._1*t._2
  )
  n

def haveBad(screw:String) = screw.exists(_ > '2')

def wrongScrew(n:Int) = 
  var N = n
  var change = r.nextInt(2) + 2 
  var screw:List[Int] = Nil
  s2.foreach(w => 
    if change > 1 && N / w > 0 then 
      change -= 1
      screw = (N / w)::screw
      N %= w
    else if change == 1 && N / w > 0 then 
      change -= 1
      screw = 0::screw
    else if change == 0 then 
      if N / w > 2 then 
        screw = 2::screw
        N -= 2*w
      else 
        screw = (N / w)::screw
        N %= w
  )
  val wrong = screw.reverse.dropWhile(_ == 0).map(_.toString).mkString
  val rightN = fromScrew(wrong)
  //println(s"$n $rightN $wrong ${toScrew(n)}")
  if rightN == n && !haveBad(wrong) then wrong
  else "-"


def createData = 
  val n = r.nextInt(900000000) + 1000
  val screw = toScrew(n)
  val t = r.nextInt(20)
  if t > 17 then
    val wrong = wrongScrew(n)
    if wrong != "-" && wrong != screw then println(s"$n $wrong")
    else println(s"$n $screw")
  else if t == 17 then 
    println(s"${n+r.nextInt(17)} $screw")
  else println(s"$n $screw")


@main def main():Unit =
  /*
  println(toScrew(92))
  println(fromScrew("110000"))
  println(fromScrew("101202"))
  println(fromScrew("102001"))

  println("LOOKING FOR WRONG")
  for i <- 600 to 7000 do
    val w = wrongScrew(i)
    val s = toScrew(i)
    if w != "-" then println(s"FOUND: $i $s $w")
  */
  for i <- 1 to 1000 do
    createData


