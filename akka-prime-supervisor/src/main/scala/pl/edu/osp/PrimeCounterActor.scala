package pl.edu.osp

import akka.actor.{Actor, ActorLogging}

class PrimeCounterActor(id:Int) extends BaseSave with Actor with ActorLogging  {
  log.info("created PrimeCounterActor id: " + id.toString)
  var saved = false
  def receive= {
    case numb:Long => {
      val n = primeBank.check(numb)  
    		  
      if(n != 0L) {
        if(!saved){
          saved = true
          saveBank(id)
          log.info("Bank %d saved".format(id))
        }
        sender ! (n, id)
      }
    }
    case t:Boolean => {
      log.info("I will throw exception!!!!")
      throw new NumberFormatException
    }
  }

  override def preStart() {
    primeBank.loadData(loadBank(id))
  }
  override def postStop(): Unit = {
    saveBank(id)
    log.info("Aktor %d zatrzymany".format(id))
  }

}