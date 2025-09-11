import scala.util.Random

val rand = Random()

def operators = List('+', '-', '*', '/', '^')

def getLetter(letters:String):Char = 
  if letters.size <= 26 then letters.last
  else letters(rand.nextInt(letters.size-1))

def getLetterOrOperator(chNum:Int, opNum:Int, letters:String):Char =
  if chNum <= opNum + 1 then getLetter(letters:String)
  else if rand.nextFloat() > 0.5 then operators(rand.nextInt(5))
  else getLetter(letters:String)  

def newLine = 
  var onp = ""
  var letters = "a"
  val chs = rand.nextInt(97)+4
  var chNum = 0
  var opNum = 0
  for i <- 1 to chs do
     val l = getLetterOrOperator(chNum, opNum, letters)
     onp += l
     if l.toInt >= 'a'.toInt && l.toInt <= 'z'.toInt then
       chNum += 1
       if letters.size <= 26 then letters += (letters.last+1).toChar
     else opNum += 1
  onp

def toInfix(onp:String):String =
  onp


@main def main():Unit = 
  for i <- 1 to 100 do
     println(newLine)
     
