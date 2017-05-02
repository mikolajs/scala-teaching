package pl.edu.osp

import akka.actor.{ActorSystem, Props}

case class Kill()

object Boot  extends App with BaseSave {

    val system = ActorSystem("MySystem")
    val nextActor = system.actorOf(Props[PrimeCounterActor], name = "primeActor")
    println("Begins Loop")
    val rangeCount = 1000000L
    primeBank.check(2L)
    var i = 3L
    while (i < rangeCount) {
      val n = primeBank.check(i)
      if(n != 0L) nextActor ! n
      i += 2L
    }
    println("Ends Loop")
    saveBank
    Thread.sleep(10000)
    //nextActor ! Kill
    for(i <- 5 to 25 by 5){
         println("To end %s s".format(25-i))
         Thread.sleep(5000)
    }
    println("close!")
    system.terminate()


}
