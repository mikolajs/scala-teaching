package pl.edu.osp

import akka.actor.{Props, Actor}
import akka.event.Logging


class SequencerActor extends Actor {
  val log = Logging(context.system, this)
  var i = 3L
  val supervisor = context.actorOf(Props[PrimeSupervisorActor], name = "primeActor")
  log.info("Sequencer Started")
  sendNum(1000)
  def receive = {
    case Next => {
      log.info("Receive Next")
      sendNum(1000)
    }
    case
      Repeat(n) => if(n % 2 == 0) i = n +1 else i = n
  }

  private def sendNum(howMany:Int): Unit = {
    for(j <- 1 to howMany) {
      supervisor ! i
      i += 2L
      Thread.sleep(0, 100)
    }
  }
}
