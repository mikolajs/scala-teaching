#!/bin/sh
exec scala shebang "$0" "$@"
!#
import java.util.*
import java.text.*
def printDayOfWeek(date:String) =
  val dateArr = date.split('.')
  if dateArr.size >= 3 then
    val c = GregorianCalendar(dateArr(2).toInt, dateArr(1).toInt-1, dateArr(0).toInt)
    val dow = c.get(Calendar.DAY_OF_WEEK)
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    sdf.setCalendar(c)
    val dF = sdf.format(c.getTime())
    val doy = c.get(Calendar.DAY_OF_YEAR)
    val dayName=c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
    println(s"$dF day of week: $dow, $dayName, day of year: $doy")
  else
    println("date must be in form dd.mm.yyyy")

if args.size < 1 then
    println("date must parameter of script in form dd.mm.yyyy")
else   
    printDayOfWeek(args(0))
