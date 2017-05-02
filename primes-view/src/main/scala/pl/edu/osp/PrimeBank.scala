package pl.edu.osp

class PrimeBank (max:Int) {
  var bank:scala.collection.mutable.ListBuffer[Int] = scala.collection.mutable.ListBuffer()
  def check(numb: Int): Int = {
    if (bank.isEmpty) {
      bank += numb
      return 0
    } else if (!foundFactorial(numb)) {
      if (numb <= max) {
        bank += numb
        return 0
      } else numb
    } else 0

  }

  def getSize = bank.size
	
	private def foundFactorial(numb:Int):Boolean = {
	  for(bNumb <- bank){
      if(bNumb*bNumb > numb ) return false
	    if(numb % bNumb == 0) return true
	  }
	  return false
	}

}