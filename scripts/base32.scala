import scala.util.Random

val r = Random()

def digit() = 
  val n = r.nextInt(62)
  if n < 10 then n.toString
  else if n < 36 then (n+87).toChar.toString
  else (n+29).toChar.toString

def numbers() = 
  (1 to 12).map(_ => digit()).mkString

@main def main() = 
  (1 to 12000).map(_ => numbers()).filter(_.head != '0')
   .take(10000).foreach(println)
    
