package net.brosbit

  import com.sun.net.httpserver.HttpServer
  import io.vertx.core.Handler
  import io.vertx.core.http.HttpServer
  import io.vertx.core.http.HttpServerRequest
  import io.vertx.core.logging.Logger
  import io.vertx.core.AbstractVerticle


  class SampleHttpWebServer extends AbstractVerticle {
    override def start(): Unit = {
      vertx.createHttpServer().requestHandler(req =>  {
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hellow form Vert.x in Scala 3 Dotty")
      }).listen(8008, "localhost").onSuccess(server => {
        
      println("Server: " + server.actualPort())})
    }
  }
