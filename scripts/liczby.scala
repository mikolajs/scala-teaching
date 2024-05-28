import scala.util.Random

val r = Random()


@main def main():Unit =
  var l:List[Int] = Nil
  for i <- 1 to 2000 do
    if r.nextInt(10) < 9 then println(r.nextInt(99998)+ 2) 
    else println(r.nextInt(26)+ 65) 
