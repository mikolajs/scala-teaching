package pl.edu.osp


trait BaseSave  {

  val primeBank:PrimeBank = new PrimeBank(4000)
  private val filePath = System.getProperty("user.dir") + "/Primes"

  def saveBank {
    println("Size of bank:" + primeBank.bank.length)
    if(primeBank.bank.size > 0) {
      val dir = new java.io.File(filePath)
      if(!dir.exists) dir.mkdir
      println("create dir: " + dir.toString)
      val file = new java.io.File(dir + "/primies_%s.txt".format(primeBank.bank.head.toString))
      val output = new java.io.BufferedWriter(new java.io.FileWriter(file))
      output.write(primeBank.bank.reverse.mkString(", "))
      println("saved bank in file: " + file.toString)
      output.close()
    }
  }

  def loadBank: List[Long] = {
    import scala.io.Source
    Source.fromFile(filePath).getLines().toList.map(_.toLong)
  }

}
