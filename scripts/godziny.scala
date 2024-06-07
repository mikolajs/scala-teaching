import java.nio.file.{Paths, Files}

val rand = new scala.util.Random()

def save(file:String, data:String) =
  val p = Paths.get(file)
  Files.write(p, data.getBytes)

def mkTime(minutes:Int) = 
  val h = minutes / 60
  val m = minutes % 60
  val hStr = if h < 10 then s"0$h" else s"$h"
  val mStr = if m < 10 then s"0$m" else s"$m"
  s"$hStr:$mStr"

def mkWorktime() =
  val sh = 6*60 + rand.nextInt(60*8) 
  val eh = sh + 6*60 + rand.nextInt(4*60)
  s"${mkTime(sh)} ${mkTime(eh)}"
  

@main
def main():Unit =
  var str = ""
  for i <- 0 to 99 do
    str += mkWorktime()
    str += "\n"
  save("godziny_pracy.txt", str)
