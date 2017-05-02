package pl.edu.osp

import akka.actor.{Actor}

case class Counted(n:Int, id:Int)

class PrimeActor(id:Int, max:Int) extends Actor  {
  val primeBank:PrimeBank = new PrimeBank(max)
  var finished = false
  def receive= {
    case numb:Int => {
      val n = primeBank.check(numb)
      if(n != 0L) {
        if(!finished && n > max) {
          finished = true
          sender ! Counted(primeBank.getSize, id)
        }
        sender ! (n, id)
      }
    }
    case Stop => {
      finished = true
      sender ! Counted(primeBank.getSize, id)
    }
  }

}