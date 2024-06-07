import scala.util.Random
import scala.collection.mutable.Stack

val r = new Random

def randNumb = r.nextInt(4)

val parantes = "[({<"
val st = Stack[Char]()

def mkClose(c:Char) = 
  c match 
    case '(' => ')'
    case '[' => ']'
    case '<' => '>'
    case '{' => '}'
    case _ => ' '

def generateGood(n:Int) = 
  var str = ""
  for i <- 0 to n do
    for i <- 1 to r.nextInt(3) do str += (('a'.toInt + r.nextInt(26)).toChar)
    if r.nextInt(2) == 0 then 
      val c = parantes(randNumb)
      str += c
      st.push(c)
    else 
      if st.size > 0 then
        val c = st.pop
        str += mkClose(c)
  while st.nonEmpty do
    str += mkClose(st.pop)
  str

@main def start() =
  (1 to 200).foreach(i =>
    if r.nextInt(2) == 0 then 
      println(generateGood(100))
    else 
      val g = generateGood(100)
      val c = parantes(randNumb)
      val n = r.nextInt(50)+50 
      println(g.take(n) + mkClose(c) + c + g.drop(n))
  )
  ""
