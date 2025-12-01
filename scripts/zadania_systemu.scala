import java.nio.file.*

val rand = new scala.util.Random()
def save(file:String, data:String) =
  val p = Paths.get(file)
  Files.write(p, data.getBytes)

def mkTime(seconds:Int) = 
  val h = seconds / 3600
  val m = (seconds % 3600) / 60
  var s = seconds % 60
  val x = s / 5
  s = x*5
  s"$h $m $s"

def mkWorktime() =
  val nwd = rand.nextInt(24)+1
  val t1 = rand.nextInt(42000/nwd)*nwd 
  val t2 = rand.nextInt(42000/nwd)*nwd
  s"${mkTime(t1)} ${mkTime(t2)}"
  

@main
def main():Unit =
  var str = ""
  for i <- 0 to 499 do
    str += mkWorktime()
    str += "\n"
  save("zadania_systemu.txt", str)
