import scala.util.Random

val r = Random()

def rand = r.nextInt(990001)+100
def randF = r.nextDouble()*100.0

@main def main():Unit =
  for i <- 1 to 500 do
     println(s"$rand")


