package eu.dgt

import akka.NotUsed
import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import akka.http.scaladsl.model.ws._
import akka.stream.{ ActorMaterializer, OverflowStrategy }
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives
import akka.stream.scaladsl._

import scala.concurrent.duration._
import scala.util.Try

class WebSocketRoute(id: String, info: Info)(implicit actorSystem: ActorSystem, materializer: ActorMaterializer)
  extends Directives {

  val route =
    path("progress") {
      handleWebSocketMessages(info.wsFlow)
    }
}

class Info(actorSystem: ActorSystem) {

  val act: ActorRef = actorSystem.actorOf(Props(new InfoActor))

  def wsFlow: Flow[Message, Message, NotUsed] = {
    val sink: Sink[Message, NotUsed] = Flow[Message]
      .collect {
        case TextMessage.Strict(txt) => {
          println(txt)
          txt
        }
      }
      .to(Sink.actorRef(act, NotUsed))

    val source: Source[Message, NotUsed] = Source
      .actorRef(bufferSize = 10, overflowStrategy = OverflowStrategy.dropBuffer)
      .map((txt: String) => TextMessage.Strict(txt))
      .mapMaterializedValue(wsAct => {
        wsAct ! TextMessage.Strict("HI")
        NotUsed
      })
      .keepAlive(maxIdle = 10.seconds, () => TextMessage.Strict("keep alive"))

    Flow.fromSinkAndSource(sink, source)
  }

  def getAct = act

}

class InfoActor extends Actor {

  override def receive: Receive = {
    case txt: String if txt == "PING" => {
      println("Receive string " + txt)
      Try(
        Thread.sleep(1000))
      sender() ! "PONG"
    }
    case n: Int => println("Number " + n)
    case x => println("Unknown message " + x.toString)
  }
}

