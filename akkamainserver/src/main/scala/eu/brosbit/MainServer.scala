package eu.brosbit

import java.net._
import akka.actor.{ActorSystem, Actor, ActorRef, Props, PoisonPill}
import akka.io.{IO, Udp, UdpConnected}
import akka.util.ByteString

import scala.util.Failure
import scala.util.Success

case class PingData(data:String, add:InetSocketAddress)

object Listener {
  def props(saveActor:ActorRef) = Props(new Listener(saveActor))
}

class Listener(saveActor:ActorRef) extends Actor {
    import context.system
    IO(Udp) ! Udp.Bind(self, new InetSocketAddress("localhost", 11111))

    def receive = {
      case Udp.Bound(local) =>
        println("recive ping")
        context.become(ready(sender()))
    }

    def ready(socket: ActorRef): Receive = {
      case Udp.Received(data, remote) =>
        val pingData = PingData(data.utf8String, remote)
        socket ! Udp.Send(ByteString.fromString("PONG"), remote) 
        saveActor ! pingData
      case Udp.Unbind  => socket ! Udp.Unbind
      case Udp.Unbound => context.stop(self)
    }
}

class SaveActor() extends Actor {
  import context.system
  def receive = {
    case pd:PingData => save(pd.data, pd.add.getAddress().getHostAddress(), pd.add.getPort().toString())
  }
  def save(d:String, host:String, port:String) = {
    import java.nio.file.Paths
    import java.nio.file.Files
    Files.write(Paths.get("/home/ms/ips.txt"), s"$d -> $host:$port".getBytes)
  }
}

object SimpleSender {
  def props(r: InetSocketAddress) = Props(new SimpleSender(r))
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

object MainServer extends App {
  val system = ActorSystem("mainServerSystem")
  val saveActor =  system.actorOf(Props[SaveActor]())
  val listener = system.actorOf(Listener.props(saveActor), "listener")
  val simpleSender = system.actorOf(SimpleSender.props(new InetSocketAddress("localhost", 22222)))
  simpleSender ! "PING"
}
