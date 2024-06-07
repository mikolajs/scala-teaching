import scala.util.Random
import java.nio.file.{Files, Paths}
val r = Random()



val S = List(5, 10, 19, 28, 50, 101, 299, 505, 1345, 99999)

def ciphers(n:Int) = 
  for i <- 1 to n yield
    r.nextInt(10)

def mkTest(n:Int, max:Int) = 
  var testC:List[Int] = Nil
  if max > 50 then 
    while testC.size < n do
      testC = r.nextInt(max+1)::testC     
      testC = testC.distinct
  else 
    var list =(0 to max+1).toList
    while testC.size < n do
      val draw = r.nextInt(list.size)
      testC =  list(draw)::testC
      list = list.take(draw) ++ list.drop(draw+1)
  testC


def mkOneTestData(s:Int) =
  val cs = ciphers(s)
  val sum = cs.sum
  val tests = if sum < 20 then r.nextInt(sum/2) + sum/2 else 20
  val line1 = s"$s $tests\n" 
  val line2 = mkTest(tests, sum).mkString(" ")
  line1 + cs.mkString(" ") + "\n" + line2 + "\n"




@main def start():Unit = 
  val data = for s <- S yield  mkOneTestData(s)
  val string  = data.mkString
  val path = Paths.get("podzialy.txt")
  Files.write(path, string.getBytes)
  
