import scala.util.Random
import scala.io.Source
import java.time.LocalDate
import java.nio.file.{Files, Paths}

val rand = Random()

def notHaveNeighbours(r:Int, c:Int, board:Array[Array[Char]]):Boolean =
  for i <- -1 to 1 do
    for j <- -1 to 1 do
      if j != 0 || i != 0 then
        val R = r + i
        val C = c + j
        if R >= 0 && C >= 0 && R < board.size && C < board.size && board(R)(C) == 'O' then return false
  return true

def canAdd(r:Int, c:Int, board:Array[Array[Char]], masts:List[(Int, Int)]):Boolean = 
  if r < 0 || c < 0 || r >= board.size || c >= board.size then  false
  else if board(r)(c) == 'O' then false
  else if notHaveNeighbours(r, c, board) && !masts.contains((r, c))  then true
  else false

def canAddNext(r:Int, c:Int, board:Array[Array[Char]], masts:List[(Int, Int)]):(Int, Int) = 
  var d = rand.nextInt(4)
  for i <- 1 to 4 do
    d match { 
      case 0 if canAdd(r-1, c, board, masts) => return (r-1, c)
      case 1 if canAdd(r, c+1, board, masts) => return (r, c+1)
      case 2 if canAdd(r+1, c, board, masts) => return (r+1, c)
      case 3 if canAdd(r, c-1, board, masts) => return (r, c-1)
      case _ => 
    }
    d += 1
    d %= 4
  return (-1, -1)

def addShip(N:Int, s:Int, board:Array[Array[Char]]):Unit = 
  var masts  = s
  var l:List[(Int,Int)] = Nil
  var r = -1 
  var c = -1 
  while true do
    while
      r = rand.nextInt(N)
      c = rand.nextInt(N)
      !canAdd(r, c, board, l) 
    do ()
    //println(s"tring add $s maszty $masts, ($r,$c) boardSize: ${board.size}")
    l = (r,c)::l
    masts -= 1
    var continue = true
    while masts > 0 && continue do
      val t = canAddNext(r, c, board, l)
      if t._1 != -1 then
        masts -= 1
        l = t::l
        r = t._1
        c = t._2
      else continue = false
    if !continue then
      l = Nil
      masts = s
    else 
      l.map(t => board(t._1)(t._2) = 'O')
      return
  
def drawShips(N:Int, ships:List[Int]) = 
  val board = Array.ofDim[Char](N, N).map(a => a.map(_ => '.'))
  for s <- ships do
    addShip(N, s, board)
  board.map(a => a.mkString("")).mkString("\n")

def randomShips(s:Int):List[Int] = 
  var size = s
  var l:List[Int] = Nil  
  while size > 0 do
    val e = rand.nextInt(4)+1
    size -= e
    l = e::l
  l

def randomShotsForOneGamer(N:Int, nr:Int) = 
  val arr = Array.ofDim[Boolean](N,N)
  var l:List[(Int, Int)]= Nil
  for i <- 1 to nr do
    var finding = true
    while finding do
      val r = rand.nextInt(N)
      val c = rand.nextInt(N)
      if !arr(r)(c) then
        finding = false
        arr(r)(c) = true
        l = (r,c)::l
  l
   
def toCoordinates(shots:List[(Int, Int )]) =
  shots.map(t => (t._2 + 65).toChar.toString + (t._1+1).toString)

def randomShots(N:Int) = 
  val nr = rand.nextInt(N*N/5) +3*N*N/4
  val gamer1 = randomShotsForOneGamer(N, nr)
  val gamer2 = randomShotsForOneGamer(N, nr)
  var str = nr.toString
  str += "\n"
  str += toCoordinates(gamer1).mkString(" ")
  str += "\n"
  str += toCoordinates(gamer2).mkString(" ")
  str += "\n"
  str


def createTwoBoards = 
  var str = ""
  val N = rand.nextInt(17)+10
  str += N.toString
  str += "\n"
  val ships = randomShips(N*N/5).sortWith((a,b) => a > b)
  str += drawShips(N, ships)
  str += "\n"
  str += drawShips(N, ships)
  str += "\n"
  str += randomShots(N)
  str


  
@main def main():Unit =
  val path = Paths.get("okrety.txt")
  var str = ""
  for i <- 1 to 10 do
    str += createTwoBoards
  Files.write(path, str.getBytes)
   



