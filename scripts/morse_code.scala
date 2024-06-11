val code = "WBZFHKQGXVMTIJNURLPEAOSDYC"

def toMorse(num1:Int) = 
  var num = num1
  var str = ""
  var p = 15
  var p2 = 23
  var n = 8
  var s = 4
  while s > 0 do
    if num >= p then
      if num >= p2 then
        str += "-"
        num -= n*2
      else 
        str += "."
        num -= n
    n /= 2
    p /= 2
    p2 /= 2
    s -= 1
  str

def toNumber(str:String) = 
  var p = 1
  var n = 0
  for a <- str.reverse do
    if a == '.' then n += p else n += p*2
    p *= 2
  n

def positions = 
  val arr = Array.ofDim[Int](26)
  var nr = 1  
  for a <- code do
    arr(a.toInt - 'A'.toInt) = nr
    nr +=1
  arr

import scala.util.boundary, boundary.break

def onlyAscii(str:String):Boolean = 
  boundary:
    for a <- str do
      if a.toInt > 127 then break(false)
    true;

def readFirstNames = 
  import scala.io.Source
  val lines = Source.fromFile("imiona.csv").getLines.toList
  lines.map(_.split(",")).map(_.head).filter(onlyAscii).take(5000)

@main def main():Unit = 
  val pos = positions
  for name <- readFirstNames do
    val codes = name.map(ch => toMorse(pos(ch.toInt-65))).mkString(" ")
    val nameCheck = codes.split(" ").map(c => toNumber(c)).map(nr => code(nr -1)).mkString
    println(s"$name: $codes =?= $nameCheck")

  //for(i <- 1 to 30) println(s"$i ${toMorse(i)} ${toNumber(toMorse(i))}")
  //println(positions.mkString(" "))

