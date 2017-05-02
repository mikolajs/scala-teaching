package pl.edu.osp

import akka.actor.{ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration._

case class Kill()

object Boot  extends App  {

    val system = ActorSystem("MySystem")
    val supervisor = system.actorOf(Props[SequencerActor], name = "seqActor")
    println("Start Application")
  Await.result(system.whenTerminated, Duration.Inf )
}

