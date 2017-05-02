package pl.edu.osp

import akka.actor.{Actor, ActorRef, Props}
import akka.event.Logging

class PrimeCounterActor extends Actor  with BaseSave {
  var log = Logging(context.system, this)
  import context._
  var nextCounter:ActorRef = null

  def receive= {
    case numb:Long => {
      val n = primeBank.check(numb)

      if(n != 0L) {
        if(nextCounter == null) {
          nextCounter = actorOf(Props[PrimeCounterActor], name = "PrimeCounter")
          log.info("created new Prime Actor")
        }
        nextCounter ! n
      }
    }
  }

  override def postStop(): Unit = {
    saveBank
  }

}
