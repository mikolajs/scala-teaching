package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import io.vertx.core.http.HttpServerResponse

import scala.io.StdIn.readLine

object Main {
  def main(args: Array[String]): Unit =
    val vertx = Vertx.vertx()
    val temperatureRouter = TemperatureRouter(vertx)
    //val fs = vertx.fileSystem()
    //val afile = fs.openBlocking("index.html", OpenOptions())
    //val gios = ClientGios()
    //println(afile.getReadLength)
    val server = vertx.createHttpServer()
    hello
    server.requestHandler(
      temperatureRouter.router
    ).listen(8989, "192.168.0.120").onSuccess(server => {
      println("Server: " + server.actualPort())
    }).onFailure(error => {
      println(s"Cannot set server ${error.toString}")
      System.exit(1)
    })

  def hello: Unit =
    println("Hello world!")
    println(msg)

  def msg = "I was compiled by Scala 3. :)"

}

