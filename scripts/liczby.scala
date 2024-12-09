import scala.util.Random

val r = Random()

def rand = r.nextInt(99001)+1000
def randF = r.nextDouble()*100.0

@main def main():Unit =
  for i <- 1 to 1000 do
     println(s"$randF")


