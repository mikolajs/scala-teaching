#!/bin/sh
exec scala "$0" "$@"
!#

println("Count pages for printing. Two pages per sheet.")

if(args.length < 2) {
  println("Pass two arguments start page and end page")
  System.exit(0);
}

val (start, endDeclared) = if(args(0).toInt < args(1).toInt) (args(0).toInt, args(1).toInt) else (args(1).toInt, args(0).toInt)
val rest = if((endDeclared - start + 1) % 4 == 0) {0} else {4 - ((endDeclared - start + 1)) % 4 }
val end = endDeclared + rest  
var orderOdd:List[Int] = Nil
var orderEven:List[Int] = Nil
val quoter = (end - start + 1) / 4
println("end: " + end + " quoter: " + quoter);

for(i <- 0 until quoter){
  orderOdd= (end - 2*i )::orderOdd
  orderOdd = (2*i+start)::orderOdd
  orderEven= (2*i+start+1)::orderEven
  orderEven = (end-1 -2*i )::orderEven
}
println(orderOdd.reverse.mkString(","))
println(orderEven.reverse.mkString(","))

