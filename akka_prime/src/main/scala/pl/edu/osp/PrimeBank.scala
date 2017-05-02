package pl.edu.osp

class PrimeBank (size:Int) {
	var bank:scala.collection.mutable.ListBuffer[Long] = scala.collection.mutable.ListBuffer()

  //if not full return 0L, else it's prime and can't store
  def check(numb: Long): Long = {
    if (bank.isEmpty) {
      bank += numb 
      return 0L
    } else if (!foundFactorial(numb)) {
      if ((bank.head * bank.head) > numb && bank.size < size) {
        bank += numb
        return 0L
      } else numb
    } else 0L

  }
	
	private def foundFactorial(numb:Long):Boolean = {
	  for(bNumb <- bank){
        if(bNumb * bNumb > numb) return false
	    if(numb % bNumb == 0) return true
	  }
	  return false
	}
}
