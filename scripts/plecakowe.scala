
val book:List[(Double, Int, Double)] = Map(4.0->164, 3.0->144, 2.0 -> 82, 1.0-> 30, 0.5 -> 21) 
  .toList.map(e => (e._1, e._2, e._2.toDouble/e._1))
val max = 5.0

def greedMaxDiv = 
  val mapDiv = book.sortWith((e1,e2) => e1._3 > e2._3)  
  var w = 0.0
  var s = 0.0
  mapDiv.foreach(e => 
    if w + e._1 <= max then 
      w += e._1
      s += e._2
      println(s"${e._1}kg i ${e._2}zł ")
    )
  println(s"Razem: $s zł i waga $w")

def greedMinW = 
  val hs = book.sortWith((e1,e2) => e1._1 < e2._1) 
  var w = 0.0
  var s = 0.0
  hs.foreach(e =>
    if w + e._1 <= max then 
      w += e._1
      s += e._2
      println(s"${e._1}kg i ${e._2}zł ")
    )
  println(s"Razem: $s zł i waga $w")
   

def greedMaxW = 
  val hs = book.sortWith((e1,e2) => e1._1 > e2._1) 
  var w = 0.0
  var s = 0.0
  hs.foreach(e =>
    if w + e._1 <= max then 
      w += e._1
      s += e._2
      println(s"${e._1}kg i ${e._2}zł ")
    )
  println(s"Razem: $s zł i waga $w")

def greedMaxC = 
  val hs = book.sortWith((e1,e2) => e1._2 > e2._2) 
  var w = 0.0
  var s = 0.0
  hs.foreach(e =>
    if w + e._1 <= max then 
      w += e._1
      s += e._2
      println(s"${e._1}kg i ${e._2}zł ")
    )
  println(s"Razem: $s zł i waga $w")

@main def main:Unit = 
  book.foreach(println)
  println("Max division")
  greedMaxDiv 
  println("min weight")
  greedMinW
  println("max weight")
  greedMaxW
  println("max cost")
  greedMaxC
