package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import scala.io.StdIn.readLine

object Main {
    def main(args:Array[String]):Unit =
        val vertx = Vertx.vertx()
        val fs = vertx.fileSystem()
        val afile = fs.openBlocking("index.html", OpenOptions())
        println(afile.getReadLength)
        vertx.createHttpServer().requestHandler(req =>  {
            println(req.path())
            if req.path() == "/get" then req.response()
              .putHeader("content-type", "application/json")
              .putHeader("Access-Control-Allow-Origin", "*")
              .end("[]")
            else req.response()
              .putHeader("content-type", "application/json")
              .putHeader("Access-Control-Allow-Origin", "*")
              .end("[]")
        }).listen(9900, "localhost").onSuccess(server => {
            println("Server: http://localhost:" + server.actualPort())})
        readLine()

    
}

