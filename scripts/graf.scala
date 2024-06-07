import scala.collection.mutable.*

val size = 21 
val neighbours = 5 

case class Town(x:Int, y:Int, n:Int):
  def info = print(s"$n at ($x, $y) \n")

var connections = ArrayBuffer[(Town, Town, Int)]()

val r = scala.util.Random()

def getPoint(size:Int):Town = Town(r.nextInt(size), r.nextInt(size), -1)

def countDistance(t1:Town, t2:Town) = 
  math.ceil(math.sqrt((t1.x - t2.x)*(t1.x - t2.x) + (t1.y - t2.y)*(t1.y - t2.y))).toInt


def createRandomPoints(nr:Int):List[Town] = 
  var towns = List[Town]()
  var left = nr
  while left > 0 do
    val p = getPoint(nr*2)
    if towns.forall(t => t.x != p.x && t.y != p.y && countDistance(t, p) > 2) then 
      towns = p :: towns
      left -= 1
  towns


def myRound(d:Double) = math.round(d*10000000.0)/10000000.0

def containPoint(t1:Town, t2:Town, X:Double, Y:Double):Boolean = 
  val T1 = if t1.x < t2.x then t1 else t2 
  val T2 = if t1.x < t2.x then t2 else t1 
  if T1.x < X && T2.x > X then return true
  else return false 
  
def notCrossed(t1:Town, t2:Town, t3:Town, t4:Town):Boolean = 
  //if t1.name == "C" then println("check C ")
  val A1 = (t1.y - t2.y.toDouble)/(t1.x - t2.x)
  val B1 = if A1.isInfinite then t1.x.toDouble else t1.y - A1*t1.x
  val A2 = (t3.y - t4.y.toDouble)/(t3.x - t4.x)
  val B2 = if A2.isInfinite then t3.x.toDouble else t3.y - A2*t3.x
  if A1 == A2 then 
    //println(s"!!! A1===A2 ${t1.name} ${t2.name} ${t3.name} ${t4.name}")
    //println(s"A1 = $A1, A2 = $A2, B1 = $B1, B2 = $B2")
    if B1 != B2 then return true
    else return false
  val X = myRound((B2 - B1)/(A1 - A2))
  val Y = A1*X + B1
  //println(s"${t1.name} ${t2.name} ${t3.name} ${t4.name}")
  //println(s"A1 = $A1, A2 = $A2, B1 = $B1, B2 = $B2, X = $X, Y = $Y")
  if containPoint(t1, t2, X, Y) && containPoint(t3, t4, X, Y) then return false
  else return true

def notCrossedCheck(t1:Town, t2:Town):Boolean = 
  //if t1.name == "A" then println(" i try for A " + t2.name) 
  if t1.x == t2.x && t1.y == t2.y then return false
  else return connections.forall(tuple => notCrossed(t1, t2, tuple._1, tuple._2))

def notExists(t1:Town, t2:Town):Boolean = 
  !connections.exists(con => (con._1.n == t1.n && con._2.n == t2.n) || (con._1.n == t2.n && con._2.n == t1.n)) 

def mapNames(points:List[Town]):List[Town] = 
  val l = math.ceil(math.log(points.length)/math.log(26.0)).toInt
  val nodesList = (1 to points.size).toList
  points.zip(nodesList).map(t => Town(t._1.x, t._1.y, t._2))
  
def findNearest(towns:List[Town], town:Town):Unit = 
  val sorted = towns.sortWith((t1, t2) => countDistance(t1, town) < countDistance(t2, town)).drop(1).take(neighbours)
  var nmb = 0 
  var trys = 0
  while trys < 10 && nmb < 2 do
    val choisenTown:Town = sorted(r.nextInt(neighbours))
    if notExists(town, choisenTown) && notCrossedCheck(town, choisenTown) then 
      connections += ((town, choisenTown, countDistance(town, choisenTown)))
      nmb += 1
    trys += 1

def findAllPossible(towns:List[Town]):Unit = 
  for t <- towns do
    val sorted = towns.sortWith((t1, t2) => countDistance(t1, t) < countDistance(t2, t)).drop(1)
    //println(sorted)
    for s <- sorted do
      if notExists(t, s) && notCrossedCheck(t, s) then
        connections += ((t, s, countDistance(t, s)))

def countNumberOfConnectionsInTowns(towns:List[Town]) = 
  towns.map(t => (t.n, connections.filter(tuple => tuple._1.n == t.n || tuple._2.n == t.n).size))
   .sortWith((t1, t2) => t1._2 > t2._2).filter(tuple => tuple._2 > 0)

def writeToFile(p:String, data:String) =
  java.nio.file.Files.write(java.nio.file.Paths.get(p), data.getBytes)
  
   
///TODO: looking connection for one edge connected
def run():Unit =
  var notOk = true
  while(notOk) do
    connections.clear 
    val points = createRandomPoints(size)
    val towns = mapNames(points)
    //towns.foreach(println)
    //findAllPossible(towns)
    println("generated: " + towns.size)
    for i <- 1 to 8 do
      towns.foreach(t => findNearest(towns, t))
    val connected = countNumberOfConnectionsInTowns(towns)
    val withOneConnection = connected.filter(_._2 % 2 != 1)
    findAllPossible(towns.filter(t => withOneConnection.exists(_._1 == t.n)))
    //connected.foreach(tw => println(s"${tw._1} : ${tw._2} "))
    //println("connections " + connections.size)
    //println("connected towns: " + connected.size)
    //println("connection with 1 edge " + withOneConnection.size)
    import scala.collection.immutable.Vector
    val graf = (0 to size).map(_ => Vector[Int]()).toArray
    connections.foreach(tup => {
      graf(tup._1.n) = graf(tup._1.n) :+ tup._2.n 
      graf(tup._2.n) = graf(tup._2.n) :+ tup._1.n 
    })
    graf.foreach(v => println(v.size))
    val even = graf.drop(1).map(v => if v.size % 2 == 0 then 1 else 0).sum
    println(even)
    if even >= size - 2  then notOk = false

  val str = s"${connections.size}\n" + connections.map(tuple => tuple._1.n + " " + tuple._2.n + " " + tuple._3).mkString("\n")
  writeToFile(s"komiwojazer_$size.txt", str)
  
  /* not working method
  val townsOneConnect = towns.filter(t => withOneConnection.exists(w => w._1 == t.name))
  println("towns one filter " + townsOneConnect.size)
  for i <- 2 to 4 do
    townsOneConnect.foreach(t => findNearest(towns, t, i))
  val connected2 = countNumberOfConnectionsInTowns(towns).filter(_._2 > 0)
  println("connections after" + connections.size)
  println("connected after towns: " + connected2.size)
  println("connection after with 1 edge " + connected2.filter(_._2 == 1).size)
  */
  
def test():Unit = 
  val towns = List(Town(7, 8, 1), Town(9, 7, 2), Town(13, 10, 3), Town(4, 11, 4), Town(4, 8, 5),
    Town(3, 5, 6), Town(5, 6, 7), Town(7, 5, 8), Town(9, 4, 9))
  println("Distance " + countDistance(towns(1), towns(7)))
  println("Distance " + countDistance(towns(7), towns(1)))
  println("Distance " + countDistance(towns(7), towns(8)))
  println("Distance " + countDistance(towns(8), towns(7)))
  println("Not  crossed? " + notCrossed(towns(1), towns(7), towns(7), towns(8)))
  findAllPossible(towns)
  connections.foreach(tuple => println(tuple._1.n + " " + tuple._2.n + " " + tuple._3))



@main def main():Unit =
  //test()
  run()

