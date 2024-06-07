import scala.util.Random
import java.nio.file.{Files, Paths}
val r = new Random

val n = 100000
val m = 1000
val k = 5621
val s = 87489

def mkRandomColor = r.nextInt(m)+1

def createBeautyColor(data:List[Int]) = 
  val arr = Array.ofDim[Int](m)
  val dataStart = data.drop(s).take(k)
  dataStart.foreach(d =>
    arr(d-1) += 1
  )
  arr

@main def start():Unit =
  val data = (1 to n).map(i => mkRandomColor).toList
  val path = Paths.get("kolorowy_lancuch.txt")
  val arr = createBeautyColor(data)
  println(arr.mkString(" "))
  Files.write(path, (n.toString + " " + m.toString + "\n" + arr.mkString(" ") + "\n" + data.mkString(" ")).getBytes)

