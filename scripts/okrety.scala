import scala.util.Random
import scala.io.Source
import java.time.LocalDate
import java.nio.file.{Files, Paths}

val r = Random()

def notHaveNeighbours(r:Int, c:Int, board:Array[Array[Int]]):Boolean =
  for i <- -1 to 1 do
    for j <- -1 to 1 do
      if j != 0 && i != 0 do
        if i >= 0 && j >= 0 && i < board.size && j < board.size && board(i)(j) then return false
  return true

def addShip(N:Int,s, board:Array[Array[Int]]):Unit = 
  while true do
    val r = r.nextInt(N)
    val c = r.nextInt(N)
    val l:List[(Int,Intr)] = Nil
    if board(r)(c) == 0 && notHaveNeighbours(r, c) then
      board(r)(c) = 1
      if s == 1 then return
      var d = r.nextInt(4)
        d match do
        case 0 if r-1 > 0 && notHaveNeighbours(r-1, c, board) => ??? 

def drawShips(N:Int, ships:List[Int]) = 
  val board = Array.ofDim[Int](N, N)
  for s <- ships do
    addShip(N, s, board)
  board.map(a => a.mkString("")).mkString("\n")

def randomShips(s:Int):List[Int] = 
  var size = s
  var l:List[Int] = Nil  
  while size > 0 do
    val e = r.nextInt(4)+1
    size -= e
    l = e::l
  l

def createTwoBoards = 
  var str = ""
  val N = r.nextInt(17)+10
  str += N.toString
  str += "\n"
  val ships = randomShips(N*N/5).sortWith((a,b) => a < b)
  str += drawShips(N, ships)
  str += "\n"
  str += drawShips(N, ships)
  str += "\n"
  str
  
@main def main():Unit =
  val path = Paths.get("okrety.txt")
  var str = ""
  for i <- 1 to 10 do
    str += createTwoBoards
  Files.write(path, str.getBytes)
   



