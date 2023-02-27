package eu.brosbit

import zhttp.http._
import zhttp.service.Server
import zio._
import zhttp.service.server.ServerChannelFactory
import zhttp.service.EventLoopGroup

object MainApp extends App {
  val app: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> !! / "text" => Response.text("Hello World!")
    case Method.GET -> !! / "json" => Response.json("""{"greetings": "Hello World!"}""")
  }

  val staticFiles: HttpApp[Any, Nothing] = Http.collectHttp[Request] {
    case Method.GET -> !! / "" => Http.fromFile(StaticFiles.getIndex).orElse(Http.empty)
    case Method.GET -> !! / name => Http.fromFile(StaticFiles.getHtml(name)).orElse(Http.empty)
    case Method.GET -> !! / "css" / name => Http.fromFile(StaticFiles.getCss(name)).orElse(Http.empty)
    case Method.GET -> !! / "js" / name => Http.fromFile(StaticFiles.getJavaScript(name)).orElse(Http.empty)
    case Method.GET -> !! / "img" / name => Http.fromFile(StaticFiles.getImg(name)).orElse(Http.empty)
  }
  ServerChannelFactory.auto

  private val server =
    Server.port(8090) ++ Server.maxRequestSize(8 * 1024) ++  Server.app(staticFiles ++ app)   

  // Run it like any simple app
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    server.make
    .use(start =>
      console.putStrLn(s"Server started on port http://localhost:${start.port}")
      *> ZIO.never
    ).provideCustomLayer(ServerChannelFactory.auto ++ EventLoopGroup.auto(2))
    .exitCode
   
}
