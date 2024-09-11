package eu.brosbit.homeserverzio

import zio._
import zio.http._

object MainApp extends ZIOAppDefault:

  // A route that matches GET requests to /greet
  // It doesn't require any service from the ZIO environment
  // so the first type parameter is Any
  // All its errors are handled so the second type parameter is Nothing
  val greetRoute: Route[Any, Nothing] =
    // The whole Method.GET / "greet" is a RoutePattern
    Method.GET / "greet" ->
      // The handler is a function that takes a Request and returns a Response
      handler { (req: Request) =>
        val name = req.queryParamToOrElse("name", "World")
        Response.text(s"Hello $name!")
      }

  // A route that matches POST requests to /echo
  // It doesn't require any service from the ZIO environment
  // It is an unhandled route so the second type parameter is something other than Nothing
  val echoRoute: Route[Any, Throwable] =
    Method.POST / "echo" -> handler { (req: Request) =>
      req.body.asString.map(Response.text(_))
    }
    
  val showTemperatureRoute: Route[Any, Throwable] =
    Method.GET / "getTemperatures" -> handler { (req: Request) =>
      val temp = DataConnect.getTemperatures.map(t => s""" {"${t._1}": ${t._2} }""").mkString(",")
      Response.json(s"""{"temperatureRooms":[$temp]}""")
    }

  // The Routes that don't require any service from the ZIO environment,
  // so the first type parameter is Any.
  // All the errors are handled by turning them into a Response.
  val routes: Routes[Any, Response] =
    // List of all the routes
    Routes(greetRoute, echoRoute, showTemperatureRoute)
      // Handle all unhandled errors
      .handleError(e => Response.internalServerError(e.getMessage))

  // Serving the routes using the default server layer on port 8080
  def run = Server.serve(routes).provide(Server.default)

