package eu.dgt

//#quick-start-server
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }
import scala.io.StdIn

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

//#main-class
object QuickstartServer extends App with UserRoutes {

  // set up ActorSystem and other dependencies here
  //#main-class
  //#server-bootstrapping
  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val info = new Info(system)

  //#server-bootstrapping

  val userRegistryActor: ActorRef =
    system.actorOf(UserRegistryActor.props, "userRegistryActor")

  val wsRoute = new WebSocketRoute("a", info)
  //#main-class
  // from the UserRoutes trait
  lazy val routes: Route = userRoutes ~ wsRoute.route
  //#main-class

  //#http-server
  val serverBinding: Future[Http.ServerBinding] =
    Http().bindAndHandle(routes, "localhost", 8080)
  StdIn.readLine() // let it run until user presses return
  serverBinding
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate())

  //  serverBinding.onComplete {
  //    case Success(bound) =>
  //      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
  //    case Failure(e) =>
  //      Console.err.println(s"Server could not start!")
  //      e.printStackTrace()
  //      system.terminate()
  //  }
  //Await.result(system.whenTerminated, Duration.Inf)
  //#http-server
  //#main-class
}
//#main-class
//#quick-start-server
