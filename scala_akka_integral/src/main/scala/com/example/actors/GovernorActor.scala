package com.example.actors

import akka.actor.{OneForOneStrategy, ActorRef, Actor, Props}

import scala.collection.mutable.ArrayBuffer

import akka.actor.SupervisorStrategy._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class GovernorActor extends Actor {
  var wynik1 = 0.0
  val actorlist:Array[ActorRef] = new Array[ActorRef](4)
  var count:Int =0

  import context._
  for (i <- 0 to 3) {
    actorlist(i) = actorOf(Props[GreeterActor], name = "X" + i.toString)
    //actorlist(i) ! 7
  }
  println("rozmiar, x0, x1, step, n*array[i]")
  val rozmiar = scala.io.StdIn.readInt()
  val lista = ArrayBuffer.empty[Double]
  val x0 = scala.io.StdIn.readDouble()
  val x1 = scala.io.StdIn.readDouble()
  val step = scala.io.StdIn.readDouble()

  for (i<-0 to rozmiar) {
    lista += scala.io.StdIn.readDouble()
  }
  actorlist(0) ! (lista.toList, x0, (3.0*x0+x1)/4.0, step)
  actorlist(1) ! (lista.toList, (3.0*x0+x1)/4.0, (2.0*x0+2.0*x1)/4.0, step)
  actorlist(2) ! (lista.toList, (2.0*x0+2.0*x1)/4.0, (1.0*x0+3.0*x1)/4.0, step)
  actorlist(3) ! (lista.toList, (x0+3.0*x1)/4.0, x1, step)



  def receive = {
    case calka:Double => {
      println("governor_receive")
      wynik1+=calka.asInstanceOf[Double]
      context.stop(sender)
      println(calka)
      count+=1
      if (count==4) {
        context.system.terminate()
        context.stop(self)
      }
    }
    case s:String => println("++++++ " + s)
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = Duration(5000,"millis")) {
      case _:Throwable => {
        Restart
      }
    }
}

