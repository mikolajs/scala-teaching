import scala.util.Random

val rand = Random()

def getHandA = 
  val a = 14 + rand.nextInt(5)
  val h = 140 + a + rand.nextInt(a*2)
  s"$a $h"


@main
def main():Unit =
  (1 to 501).map(i => println(getHandA))


