package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import scala.io.StdIn.readLine

object Main {
    def main(args:Array[String]):Unit =
        //hello
        val vertx = Vertx.vertx()
        val fs = vertx.fileSystem()
        val afile = fs.openBlocking("index.html", OpenOptions())
        val gios = ClientGios()
        println(afile.getReadLength)
        vertx.createHttpServer().requestHandler(req =>  {
            println(req.path())
            if req.path() == "/get" then gios.getAirCon(736, req)
            else req.response()
              .putHeader("content-type", "application/json")
              .putHeader("Access-Control-Allow-Origin", "*")
              .end("[]")
        }).listen(8008, "localhost").onSuccess(server => {
            println("Server: " + server.actualPort())})
        readLine()

    
    def hello: Unit =
        println("Hello world!")
        println(msg)

    def msg = "I was compiled by Scala 3. :)"
    
}

