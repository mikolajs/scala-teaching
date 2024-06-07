import scala.util.Random

val aSize = 50
val r = Random()

def rand = r.nextInt(4)+1

def generateTable = 
  var list:List[Int] = Nil
  var last = 1
  while list.size < aSize do
    last += rand
    list = last::list
  end while 
  list

def generateList(end:Int) =
  for i <- 1 to aSize yield
    r.nextInt(end+20)+1
  end for



@main def start():Unit = 
  val g = generateTable
  g.reverse.foreach(n => print(n.toString+" "))
  println()
  generateList(g.head).foreach(n => print(n.toString + " "))
  println()
  
