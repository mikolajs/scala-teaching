import scala.util.Random

val r = Random()


def mkLine = 
  val t = (r.nextInt(3)+ 65).toChar
  var s = t.toString + " "
  s += r.nextInt(15).toString + " "
  val w = t match 
    case 'A' => 1
    case 'B' => 12 
    case 'C' => 33
    case _   => 1
  s += (r.nextInt(20*w) + w).toString + " "
  s += (r.nextInt(20*w) + w).toString + " "
  s += (r.nextInt(20*w) + w).toString + " "
  s += (r.nextInt(20*w) + w).toString 
  s


@main def run() = 
  val s = (1 to 30).map(i => mkLine).mkString("\n")
  println(s)
  ""

