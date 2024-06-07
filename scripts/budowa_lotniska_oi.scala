import scala.util.Random
import java.nio.file.{Files, Paths}

val r = Random()


def airport(n:Int) =
  val arr = Array.ofDim[Char](n, n)
  for i <- 0 until n do
    for j <- 0 until n do
      arr(i)(j) = '.'
  var pools = n*n
  val free = r.nextInt(pools/2) + pools/3
  while pools > free do
    for i <- 0 until n do
      for j <- 0 until n do
        if pools > free && arr(i)(j) == '.' then 
          if r.nextInt(6) < 2 then 
            arr(i)(j) = 'X'
            pools -= 1
  arr

def toPrint(arr:Array[Array[Char]]) = 
    arr.map(_.mkString).mkString("\n")

    
@main def main():Unit =
   var data = "5\n"
   for i <- 1 to 5 do
     val ap = airport(r.nextInt(40)+2) 
     data += ap.length
     data += "\n"
     data += toPrint(ap)
     data += "\n"
   val path = Paths.get("lotniska.txt")
   Files.write(path, data.getBytes)

