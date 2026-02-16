package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

object Main {
  val port = 8989
//  val ipHost = "192.168.0.218"
  val ipHost = "192.168.0.120"
  def main(args: Array[String]): Unit =
    val vertx = Vertx.vertx()
    val router = Router.router(vertx)
    val temperatureRouter = SensorRouter(vertx, router)
    val cameraRouter = CameraRouter(vertx, router)
    val pzemRouter = PZEMRouter(vertx, router)
    val wateringRouter = WateringRouter(vertx, router)
    val variousActionsRouter = VariousActionsRouter(vertx, router)
    //val fs = vertx.fileSystem()
    //val afile = fs.openBlocking("index.html", OpenOptions())
    //val gios = ClientGios()
    //println(afile.getReadLength)
    val server = vertx.createHttpServer()
    server.requestHandler(
      router
    ).listen(port, ipHost).onSuccess(server => {
        println(s"Server: http://$ipHost:$port")
    }).onFailure(error => {
      println(s"Cannot set server ${error.toString}")
      System.exit(1)
    })

}

