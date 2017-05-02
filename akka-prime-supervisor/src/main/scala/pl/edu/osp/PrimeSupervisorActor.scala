package pl.edu.osp

import akka.actor.{Props, ActorRef, Actor, OneForOneStrategy}
import akka.actor.SupervisorStrategy._
import akka.event.Logging
import scala.concurrent.duration._

case class Repeat(n:Long)
case class Next()

class PrimeSupervisorActor extends Actor  {
  val log = Logging(context.system, this)
  var num = 3L

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 second) {
      case _: Throwable   => {
        context.parent ! Repeat(lastNumber)
        log.info("restart! with last number " + lastNumber)
        Restart
      }
    }
  val ref:Array[ActorRef] = new Array[ActorRef](10)
  for(i <- 0 to 9) ref(i) = context.actorOf(Props(classOf[PrimeCounterActor], i))
  var lastNumber = 0L
  var lastActor = -1
  var passed = 0
    def receive = {
      case n:Long => {
        ref(0) ! n
        passed += 1
        if(passed >= 1000) {
          log.info("Passed is more than 1000")
          Thread.sleep(10)
          context.parent ! Next
          passed = 0
        }
      }
      case (n:Long, id:Int) => {
        //if(n == 557L) sender() ! true
        if(lastActor < id) {
          lastActor = id
          lastNumber = n
          log.info("Actor %s is full".format(id))
        }
          //it should control all numbers killed in first counter actor and request
          //next when the same number killed or passed to next actor
        if(id == 9) {
          //send to next supervisor
          log.info("Terminate actors system")
          //temporary
          Thread.sleep(10000)
          ref.foreach(r => context.stop(r))
          context.system.terminate()
          context.stop(self)
        } else {
          ref(id +1) ! n
        }
      }
    }
  override def postStop(): Unit = {
    log.info("Supervisor zatrzymany")
  }
}
