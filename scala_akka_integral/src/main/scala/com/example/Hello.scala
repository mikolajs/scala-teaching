package com.example

import com.example.actors.GovernorActor
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.actor.{ActorSystem, Props}


object HelloSimpleMain extends App {
    val system = ActorSystem("MySystem")
    val initialActor = system.actorOf(Props[GovernorActor], name = "governorActor")

    Await.result(system.whenTerminated, Duration.Inf)

}
