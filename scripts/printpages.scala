#!/bin/sh
exec scala "$0" "$@"
!#

println("Count pages for printing. Two pages per sheet.")

if(args.length < 2) {
  println("Pass two arguments start page and end page")
  System.exit(0);
}

val start = args(0).toInt
val end = args(1).toInt

val all = (start to end).toList

println(all)

