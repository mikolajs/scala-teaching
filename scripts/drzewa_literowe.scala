import scala.util.Random
import java.nio.file.{Files, Paths}
import scala.collection.mutable.Queue
val r = new Random


//def mkRandom = r.nextLong(200000000000L)-100000000000L




def randomLettersSet = 
  var list:List[Char] = Nil
  for i <- 0  to 51 do
    if r.nextInt(15) > 0 then
      if i <= 25 then list = ('A'+ i).toChar::list
      else list = ('a' + i-26).toChar::list
  r.shuffle(list) 
  

def createGraf(list:List[Char]) = 
  var mList = list 
  var con:List[(Char, Char)] = Nil
  val first = r.nextInt(mList.size)
  val q = Queue[Char]() 
  q += mList(first)
  mList = mList.take(first) ++ mList.drop(first+1)
  var levelList = r.nextInt(mList.size)
  while q.nonEmpty do
    val l = q.dequeue
    val s = r.nextInt(4)+1
    for i <- 1 to s do
      if mList.nonEmpty then
        val n = r.nextInt(mList.size)
        val aL = mList(n)
        mList = mList.take(n) ++ mList.drop(n+1)
        q += aL
        con = (aL, l)::con
  r.shuffle(con)





@main def start():Unit = 
  val n = 7
  var string = s"$n\n"
  for _ <- 1 to n do
    val letters = randomLettersSet
    val cons = createGraf(letters)
    string += s"${cons.size}\n"
    string += cons.map(t => s"${t._1} ${t._2}").mkString("\n")
    string += "\n"
  println(string)
  val path = Paths.get("drzewka.txt")
  Files.write(path, string.getBytes)
  
