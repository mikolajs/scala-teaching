package eu.brosbit

import java.net._
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.{Actor, ActorRef, Props, PoisonPill}
import akka.io.{IO, Udp, UdpConnected}
import akka.util.ByteString

import scala.util.Failure
import scala.util.Success

object Listener {
 def apply(): Behiavior[String] = Behaviors.setup(context => new Listener(context))
}


class Listener(nextActor: ActorRef) extends Actor {
    import context.system
    IO(Udp) ! Udp.Bind(self, new InetSocketAddress("localhost", 11111))

    def receive = {
      case Udp.Bound(local) =>
        //#listener
        nextActor.forward(local)
        //#listener
        context.become(ready(sender()))
    }

    def ready(socket: ActorRef): Receive = {
      case Udp.Received(data, remote) =>
        val processed = // parse data etc., e.g. using PipelineStage
          //#listener
          data.utf8String
        //#listener
        socket ! Udp.Send(data, remote) // example server echoes back
        nextActor ! processed
      case Udp.Unbind  => socket ! Udp.Unbind
      case Udp.Unbound => context.stop(self)
    }
}

class SimpleSender(remote: InetSocketAddress) extends Actor {
  import context.system
  IO(Udp) ! Udp.SimpleSender

  def receive = {
    case Udp.SimpleSenderReady =>
      context.become(ready(sender()))
  }
  def ready(send: ActorRef): Receive = {
    case msg: String =>
      send ! Udp.Send(ByteString(msg), remote)
  }
}

object MainServer {
  val listener = ActorSystem(Listener(), "mainSystem")  
  
}
