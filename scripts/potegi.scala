import scala.util.Random

val rand = Random()

@main
def main():Unit = 
  var v = 3
  println(
  //(2 to 100001).map(i => 
  //  v +=(rand.nextInt(100)+1)
  //  v
  //  ).toList.mkString(" ")

  (2L to 100001L).map(i => i*i + (rand.nextInt(5)+1)).toList.mkString(" ")
  )
