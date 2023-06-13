


def count(years:Int, p:Double):Double = 
  var s = 1.0 
  for i <- 1 to years do s = s*(1+p)
  s

@main def main(procent:Int):Unit = 
  println(count(15, procent.toDouble/100.0))
