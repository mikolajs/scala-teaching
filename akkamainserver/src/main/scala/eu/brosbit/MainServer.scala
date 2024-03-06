package eu.brosbit

import java.net._
import akka.actor.{ActorSystem, Actor, ActorRef, Props, PoisonPill}
import akka.io.{IO, Udp, UdpConnected}
import akka.util.ByteString
import scala.collection.JavaConverters._

import scala.util.Failure
import scala.util.Success
import scala.io.StdIn

object ConfigData {
  def load = {
    import java.nio.file.Paths
    import java.nio.file.Files
    val path = "/home/administrator/pingserver.cfg"
    val list = Files.readAllLines(Paths.get(path)).asScala.toList
    //....
  }
}

case class PingData(data:String, add:InetSocketAddress)

class IPsData() {
  import java.nio.file.Paths
  import java.nio.file.Files
  val path = "/home/administrator/ips.txt"
  val arr = Array.ofDim[String](20).map(_ => "")
  def openFile = {
    println("Save ips data")
    val list = Files.readAllLines(Paths.get(path)).asScala.toList
    var i = 0
    list.foreach(e => {
    	arr(i) = e
    	i += 1
    })
  }
  def save(id:Int, ip:String) = {
  	openFile
  	arr(id-1) = ip
  	println("size of array = " + arr.size)
  	Files.write(Paths.get(path), getString.getBytes)
  }
  def getString = arr.mkString("\n") 
}

object Listener {
  def props(saveActor:ActorRef) = Props(new Listener(saveActor))
}

class Listener(saveActor:ActorRef) extends Actor {
    import context.system
    IO(Udp) ! Udp.Bind(self, new InetSocketAddress("localhost", 11111))

    def receive = {
      case Udp.Bound(local) =>
        println("start Listener")
        context.become(ready(sender()))
    }

    def ready(socket: ActorRef): Receive = {
      case Udp.Received(data, remote) =>
        val pingData = PingData(data.utf8String, remote)
        println(s"received ${data.utf8String}")
        socket ! Udp.Send(ByteString.fromString("PONG"), remote) 
        saveActor ! pingData
      case Udp.Unbind  => socket ! Udp.Unbind
      case Udp.Unbound => context.stop(self)
    }
}

class SaveActor() extends Actor {
  import context.system
  val ipsData = new IPsData()
  def receive = {
    case pd:PingData => save(pd.data, pd.add.getAddress().getHostAddress(), pd.add.getPort().toString())
  }
  def save(d:String, host:String, port:String) = {
    if(d.take(5) == "PONG:" && d.size > 5) {
      	val id = d.filter(e => e >= '0' && e <= '9')
      	if(id.size > 0) ipsData.save(id.toInt, host+":"+port)
    }
    
  }
}

object Connected {
  def props(remote:InetSocketAddress) = Props(new Connected(remote))
}

class Connected(remote: InetSocketAddress) extends Actor {
  import context.system
  IO(UdpConnected) ! UdpConnected.Connect(self, remote)
  IO(Udp) ! Udp.Bind(self, new InetSocketAddress("localhost", 11111))

  def receive = {
    case UdpConnected.Connected =>
      context.become(ready(sender()))
  }

  def ready(connection: ActorRef): Receive = {
    case UdpConnected.Received(data) =>
      println("CONNECTED reveived: " + data.utf8String)
      // process data, send it on, etc.
    case msg: String =>
      connection ! UdpConnected.Send(ByteString(msg))
    case UdpConnected.Disconnect =>
      connection ! UdpConnected.Disconnect
    case UdpConnected.Disconnected => context.stop(self)
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
      println(s"send $msg from simple sender")
      send ! Udp.Send(ByteString(msg), remote)
  }
}

object MainServer extends App {
  val system = ActorSystem("mainServerSystem")
  val saveActor =  system.actorOf(Props[SaveActor](), "saveActor")
  //val listener = system.actorOf(Listener.props(saveActor), "listener")
  val connected = system.actorOf(Connected.props(new InetSocketAddress("localhost", 11112)), "connected")
  val simpleSender = system.actorOf(SimpleSender.props(new InetSocketAddress("localhost", 11111)), "simpleSender")
  Thread.sleep(5000)
  simpleSender ! "PONG:15"
  connected ! "PONG:14"
  simpleSender ! "PiNG:15"
  println("Press enter to exit...")
  StdIn.readLine()
  system.terminate()
}
