package eu.brosbit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import eu.brosbit.games.GamesPlayers
import spray.json.DefaultJsonProtocol.{StringJsonFormat, jsonFormat2}

import scala.concurrent.duration.DurationInt
import scala.io.StdIn
import scala.util.Try

object HttpServerRoutingMinimal {

    def main(args: Array[String]): Unit = {

      implicit val system = ActorSystem("ticTacToe5")
      // needed for the future flatMap/onComplete in the end
      implicit val executionContext = scala.concurrent.ExecutionContext.global

      val mongoDB = MongoDataBase
      implicit val userSignInFormat = jsonFormat2(NewUserCreate)

      val route =
        pathPrefix("login"  ) {
          concat(
            path("signin"){
              parameters("name", "password") { (aName, aPassw) =>
                get {
                  //println("FIND USER: " + aName + " " + aPassw)
                  val signInUser = mongoDB.getUser(aName, aPassw)
                  onSuccess(signInUser){
                    case Some(user) => {
                      val code = GamesPlayers.mkLogin(user.name, user.points)
                      complete(s"""{"info":"OK", "name":"${user.name}", "code":"$code"}""")
                    }
                    case _ => complete(StatusCodes.NotFound)
                  }
                }
              }
            },
            path("signon") {
              parameters("name", "password") { (aName, aPassw) =>
                get {
                  val signOnUser = mongoDB.setNewUser(aName, aPassw)
                  onSuccess(signOnUser) {
                    case user: NewUserCreate => {
                      val code = GamesPlayers.mkLogin(aName, 0)
                      complete(s"""{"info":"${user.info}", "name":"${user.name}", "code":"$code"}""")
                    }
                    case _ => complete(StatusCodes.NotAcceptable)
                  }
                }
              }
            }
          )
        } ~ pathPrefix("logout"){
          get {
            headerValueByName("code") { code =>
              complete(GamesPlayers.mkLogout(code))
            }
          }
        } ~ pathPrefix("players"){
          get {
            headerValueByName("code") { code =>
              //println("get code: " + code)
              complete(GamesPlayers.getAllPlayers(code))
            }
          }
        } ~ pathPrefix("me") {
          get {
            headerValueByName("code") { code =>
              val user = GamesPlayers.getUserInfo(code)
              if (user.isDefined) complete(user)
              else complete(StatusCodes.NotAcceptable)
            }
          }
        } ~ pathPrefix("game"){
          concat(
            path("createroom"){
              get {
                  headerValueByName("code") { code =>
                    val json = GamesPlayers.addUserToRoom(code, "")
                    if (json.isDefined) complete(json)
                    else complete(StatusCodes.NotAcceptable)
                  }
              }
            },
            path("addtoroom") {
              get {
                parameter("room"){ aRoom =>
                  headerValueByName("code") { code =>
                    val json = GamesPlayers.addUserToRoom(code, aRoom)
                    if (json.isDefined) complete(json)
                    else complete(StatusCodes.NotAcceptable)
                  }
                }
              }
            } ,
            path("checkroom") {
              get {
                parameter("room"){ aRoom =>
                  headerValueByName("code") { code =>
                    val json = GamesPlayers.checkRoom(code, aRoom)
                    if (json.isDefined) complete(json)
                    else complete(StatusCodes.NotAcceptable)
                  }
                }
              }
            } ,
            path("mymove"){
              get {
                parameters("room", "row", "col"){ (aRoom, row, col ) =>
                  headerValueByName("code") { code =>
                    if(Try(row.toInt).isSuccess && Try(col.toInt).isSuccess){
                      val json = GamesPlayers.nextMove(code, aRoom, row.toInt, col.toInt )
                      if(json.isDefined) complete(json)
                      else complete(StatusCodes.NotAcceptable)
                    } else complete(StatusCodes.NotFound)
                  }
                }
              }
            }
          )
        }

      val bindingFuture = Http().newServerAt("localhost", 9099).bind(route)
      println(s"Server online at http://localhost:9099/\nPress RETURN to stop...")
      system.scheduler.scheduleWithFixedDelay(300.seconds, 300.seconds)(
        () => {
          GamesPlayers.clearDysfunctionalRooms()
        }
      )
      StdIn.readLine() // let it run until user presses return
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
    }
}
