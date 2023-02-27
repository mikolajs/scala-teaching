package eu.brosbit

import zhttp.http._
import zhttp.service.Server
import zio.*
import zhttp.service.server.ServerChannelFactory
import zhttp.service.EventLoopGroup

object MainApp extends ZIOAppDefault {

  val action: ZIO[Any, Throwable, Unit] =
    for { 
      _ <- ZIO.attempt { println("IS?") }
      _ <- Console.printLine("Start next day")
    } yield ()

  val  repeated = action.repeat(Schedule.fixed(30.seconds)).fork

  val PORT = 8090
  val app: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> !! / "text" => Response.text("Hello World!")
  }

  val staticFiles: HttpApp[Any, Nothing] = Http.collectHttp[Request] {
    case Method.GET -> !!  => Http.fromFile(StaticFiles.getIndex).orElse(Http.empty)
    case Method.GET -> !! / name => Http.fromFile(StaticFiles.getHtml(name)).orElse(Http.empty)
    case Method.GET -> !! / "css" / name => Http.fromFile(StaticFiles.getCss(name)).orElse(Http.empty)
    case Method.GET -> !! / "js" / name => Http.fromFile(StaticFiles.getJavaScript(name)).orElse(Http.empty)
    case Method.GET -> !! / "img" / name => Http.fromFile(StaticFiles.getImg(name)).orElse(Http.empty)
  }
  ServerChannelFactory.auto

  val program: ZIO[Any, Throwable, ExitCode] = for {
    _ <- Console.printLine(s"Server started on port http://localhost:${PORT}")
    _ <- Server.start(port = PORT, http = staticFiles ++ app)
  } yield ExitCode.success

  override def run:ZIO[Environment with ZIOAppArgs with Scope,Any,Any]  = 
    repeated.zip(program)
 
 } 

