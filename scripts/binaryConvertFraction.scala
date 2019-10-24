#!/bin/sh
exec scala -nc "$0" "$@"
!#

def binaryToDecimal(data:String):String =  {
val (intPart, fractionPart)  =  data.split('.') match {
      case Array(a1, a2, rest @ _*) => (a1, a2)
      case _ => ("0", "0")
      }
 val intPartDec = Integer.parseInt(intPart, 2)
 val (fractFirmPart, fractFreqPart) =  fractionPart.split('(') match {
   case Array(a1, a2, rest @ _*) => (a1, a2.dropRight(1))
   case Array(a1) => (a1, "")
   case _ => ("0", "")
 }

  val (num, den) = binaryToPlainFraction(fractFirmPart)
  
  val (numFreq, denFreq) = binaryToPlainFraction(fractFreqPart, den-1)
  val a2powN = Math.pow(2,fractFreqPart.length).toInt;

  s"${intPartDec.toString} fraction: $num / $den} from frequency part ${numFreq} / ${denFreq - 1}"
}

def binaryToPlainFraction(binFraction:String, rel:Int = 1):(Int, Int) = {
  var p = rel 
  val array = binFraction.reverse.toArray.map(_.toString).zip((0 until binFraction.length()).map(n => {val t = p; p *= 2; t})) 
  val numerator = array.map(tup => Integer.parseInt(tup._1, 2)*tup._2).foldLeft(0)(_+_)
  val denominator = array.last._2 * 2

  (numerator, denominator)
}

def decimalFractionToBinary(data:String) = {
  val (intPart, fractionPart)  =  data.split('.') match {
      case Array(a1, a2, rest @ _*) => (a1, a2)
      case _ => ("0", "0")
      }
  val binaryIntPart = intPart.toInt.toBinaryString
}

def gnd(a:Int, b:Int):Int = {
  if(a % b == 0) b
  else gnd(b, a % b) 
}


println(binaryToDecimal("1110101.110101(1011)"))
println(gnd(24, 18))
