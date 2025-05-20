import scala.util.Random

val r = new Random

def rand = 
  val n = r.nextInt(3)
  n match
    case 0 => '.'
    case 1 => 'B'
    case _ => 'W'


def createMatrix = 
  val ar = Array.ofDim[Char](19, 19)
  for i <- 0 until 19
    j <- 0 until 19 do
    ar(i)(j) = '.'
  var row = 0
  var col = 0
  val turns = r.nextInt(40)+80
  for i <- 1 to turns do
    row = r.nextInt(19)
    col = r.nextInt(19)
    while ar(row)(col) != '.' do
      row = r.nextInt(19)
      col = r.nextInt(19)
    ar(row)(col) = 'B'
    while ar(row)(col) != '.' do
      row = r.nextInt(19)
      col = r.nextInt(19)
    ar(row)(col) = 'W'
  if r.nextInt(2) == 1 then
    while ar(row)(col) != '.' do
      row = r.nextInt(19)
      col = r.nextInt(19)
    ar(row)(col) = 'B'
  ar


@main def start():Unit =
  (1 to 10).foreach(i =>
    println(createMatrix.map(_.mkString).mkString("\n"))
  )


