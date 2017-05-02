package pl.edu.osp


class BaseSave()  {

  val primeBank:PrimeBank = new PrimeBank(100)
  val path = System.getProperty("user.dir") + "/Primes"
  def saveBank(id:Int) {
    println("Size of bank:" + primeBank.bank.length)
    if(primeBank.bank.size > 0) {
      val dir = new java.io.File(path)
      if(!dir.exists) dir.mkdir
      println("create dir: " + dir.toString)
      val file = new java.io.File(dir + "/primies_%d.txt".format(id))
      val output = new java.io.BufferedWriter(new java.io.FileWriter(file))
      output.write(primeBank.bank.mkString(", "))
      println("saved bank in file: " + file.toString)
      output.close()
    }
  }

  def loadBank(id:Int):List[Long] = {
     import scala.io.Source
    try {
        Source.fromFile(path + "/primes_%d.txt".format(id)).getLines().toList.map(_.toLong)
    } catch {
      case  e:Throwable => Nil
    }
  }

}
