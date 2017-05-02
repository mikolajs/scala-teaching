package com.example.actors

import akka.actor.{ActorRef, Actor, ActorLogging}


// Note: Usually the message object (GreeterMessages) and the actor class (GreeterActor) will be called the same thing (eg. Greeter)
object GreeterMessages {
  case object Done
  case object Stop
}

class GreeterActor extends Actor with ActorLogging {
  def horner(buffer: List[Double], x0:Double):Double = {
    var wynik = 0.0
    for (i<-buffer.indices) { //0 to buffer.length-1
      wynik = wynik * x0 + buffer(i)
    }
    wynik
  }
  def integral(buffer: List[Double], x0:Double, x1:Double, step:Double):Double = {
    var wynik = 0.0
    var value1 = 0.0; var value2 = 0.0
    value1 = horner(buffer,x0)
    for (i:Double<-x0+step to x1 by step) {
      value2=horner(buffer,i)
      wynik+=(value1+value2)*step/2.0
      value1=value2
    }
    wynik
  }
  def receive = {
    case (buffer:List[Double],x0:Double,x1:Double,step:Double) => {
      log.info("recieved signal")
      sender ! "Wysyłam dane z Greeter"
      val wynik:Double = integral(buffer, x0, x1, step)
      sender() ! wynik
      log.info("sent data " + wynik)
    }
  }

}
