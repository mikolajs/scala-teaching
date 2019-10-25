#!/bin/sh
exec scala -nc "$0" "$@"
!#
println()
if(args.length < 3) {
  println("Syntax: number base_for_number base_for_result numbers_of_meanigfull_digits(optional)")
} else {

  val bf = new BinaryFraction {
    override val fractionLong: Int = if(args.length == 4) args(3).toInt else 30
  }
  val r = bf.fromStringToFraction(args(0), args(1).toInt)
  println(bf.mkString(r._1, r._2, args(2).toInt))
}

//val bf = new BinaryFraction {
//  override val fractionLong: Int = 30}
//val r = bf.fromStringToFraction("11100110.1101101", 2)
//println(bf.mkString(r._1, r._2, 10))
//val r4 = bf.fromStringToFraction("1010110.1011011", 2)
//println(bf.mkString(r4._1, r4._2, 10))
//val r2 = bf.fromStringToFraction("21.435", 10)
//println(bf.mkString(r2._1, r2._2, 2))
//val r3 = bf.fromStringToFraction("19.245", 10)
//println(bf.mkString(r3._1, r3._2, 2))

trait BinaryFraction {
  val fractionLong = 20
  def fromStringToFraction(data: String, base:Int): (Int, Fractional)= {
    val (intPartStr, fractionPartStr) = data.split('.') match {
      case Array(a1, a2, rest@_*) => (a1, a2)
      case _ => ("0", "0")
    }
    val intPartDec = Integer.parseInt(intPartStr, base)
    val (fractFirmPartStr, fractFreqPartStr) = fractionPartStr.split('(') match {
      case Array(a1, a2, rest@_*) => (a1, a2.dropRight(1))
      case Array(a1) => (a1, "0")
      case _ => ("0", "0")
    }

    val  fractionalFirmPart = toPlainFraction(fractFirmPartStr, base)

    val fractionalRepeatPart = toPlainFraction(fractFreqPartStr, base, fractFirmPartStr.length)
    val a2powN = Math.pow(2, fractFreqPartStr.length).toInt
    val qNoverq = new Fractional(a2powN, a2powN -1)
    val fractionalRepeatPartAll =  fractionalRepeatPart * qNoverq

//    println(fractionalFirmPart)
//    println(fractionalRepeatPart)
//    println(fractionalRepeatPartAll)
    (intPartDec,  fractionalFirmPart + fractionalRepeatPartAll)
  }

  def mkString(integer:Int, fractional: Fractional, base:Int) : String = {
    val integerStr = base match {
      case 2 => integer.toBinaryString
      case 8 => integer.toOctalString
      case 10 => integer.toString
      case 16 => integer.toHexString
    }
    s"${integerStr}.${toSystemFractionalString(fractional, base )}"
  }

  private def toSystemFractionalString(f: Fractional, base:Int):String = {
    var numerator = f.numerator*base
    var steps = 0
    var expansion = ""
    while(numerator != 0 && steps < fractionLong){
      expansion += (numerator / f.denominator)
      numerator %= f.denominator
      numerator *= base
      steps += 1
    }
    expansion
  }

  private def toPlainFraction(binFraction: String,  base:Int = 2, rel: Int = 0): Fractional = {
    var p = 1
    val array = binFraction.reverse.toArray.map(_.toString).zip((0 until binFraction.length()).map(n => {
      val t = p; p *= base; t
    }))
    val numerator = array.map(tup => Integer.parseInt(tup._1, base) * tup._2).foldLeft(0)(_ + _)
    val denominator = array.last._2 *  Math.pow( base, rel + 1).toInt

    new Fractional(numerator, denominator)
  }

  def gnd(a: Int, b: Int): Int = {
    if (a % b == 0) b
    else gnd(b, a % b)
  }
}



class Fractional(n: Int, d: Int) {
  private val g = gnd(n.abs, d.abs)
  val numerator = n / g
  val denominator = d.abs / g
  def this(d: Int) = this(d, 1)

  override def toString() = {
    val c = numerator / denominator
    if(c == 0) (numerator + "/" + denominator)
    else (c + " " + (numerator.abs % denominator) + "/" + denominator)
  }

  def +(fractional: Fractional) =
    new Fractional(
      numerator * fractional.denominator  + fractional.numerator * denominator,
      denominator * fractional.denominator
    )

  def +(number: Int) =
    new Fractional(
      numerator + denominator * number, denominator)

  def -(fractional: Fractional) =
    new Fractional(
      numerator * fractional.denominator  - fractional.numerator * denominator,
      denominator * fractional.denominator
    )

  def -(number: Int) =
    new Fractional(
      numerator - denominator * number, denominator)

  def *(fractional: Fractional) =
    new Fractional(numerator * fractional.numerator, denominator * fractional.denominator)

  def *(number: Int) =
    new Fractional(numerator * number, denominator)

  def /(fractional: Fractional) = {
    val sign = if(fractional.numerator < 0) -1 else 1
    new Fractional(sign * numerator * fractional.denominator, denominator * fractional.numerator)
  }

  def /(number: Int) = {
    val sign = if(number < 0) -1 else 1
    new Fractional(sign * numerator, denominator * number)
  }

  private def gnd(a: Int, b: Int): Int = if(b == 0) a else gnd(b, a % b)
}
