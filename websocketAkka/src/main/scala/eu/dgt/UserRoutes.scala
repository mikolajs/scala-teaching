package eu.dgt

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging

import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers._

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path

import scala.concurrent.Future
import eu.dgt.UserRegistryActor._
import akka.pattern.ask
import akka.util.Timeout

//#user-routes-class
trait UserRoutes extends JsonSupport {
  //#user-routes-class

  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[UserRoutes])

  // other dependencies that UserRoutes use
  def userRegistryActor: ActorRef

  // Required by the `ask` (?) method below
  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  //#all-routes
  //#users-get-post
  //#users-get-delete

  def myComplete(ans: String) = {
    respondWithDefaultHeaders(RawHeader("Access-Control-Allow-Origin", "*")) {
      complete(ans)
    }
  }

  lazy val userRoutes: Route =
    pathPrefix("users") {
      myComplete(
        """{"users":[{"id"=2;"name"="Kowalski"}, {"id":3, "name"="Nowak"}]}""")
    }
  //#all-routes
}
