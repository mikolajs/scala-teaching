import scala.collection.mutable.*


val r = scala.util.Random()

def writeToFile(p:String, data:String) =
  java.nio.file.Files.write(java.nio.file.Paths.get(p), data.getBytes)
   
///TODO: looking connection for one edge connected
def run():Unit =
  var l:List[Int] = Nil
  for i <- 1 to 10000 do
    l = getNumber::l

  writeToFile(s"liczby_iloczynowe.txt", l.mkString("\n"))
  
def getNumber = 
  val p = r.nextDouble()
  if p > 0.5 then 
     r.nextInt(1999999999)+2
  else 
     createMultiply()

def createMultiply():Int = 
  var start = r.nextLong(1200)
  val max = r.nextInt(8)+2
  var nr = 0
  if start < 2 then start = 2
  var multi = start
  //println(start)
  while true do
    start += 1
    if nr >= max then return multi.toInt
    if multi*start > 2000000000L then return multi.toInt
    multi *= start
    //println(multi)
    nr += 1
  0


@main def main():Unit = run()
