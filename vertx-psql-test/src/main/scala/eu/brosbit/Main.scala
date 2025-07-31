package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

object Main {
  val port = 8989
  val ipHost = "192.168.0.218"
//  val ipHost = "192.168.0.120"
  def main(args: Array[String]): Unit =
    val vertx = Vertx.vertx()
    val router = Router.router(vertx)
    val dbCon = DBConnect(vertx)
    val temperatureRouter = TemperatureRouter(vertx, router, dbCon)
    val cameraRouter = CameraRouter(vertx, router, dbCon)
    val pzemRouter = PZEMRouter(vertx, router, dbCon)
    val wateringRouter = WateringRouter(vertx, router, dbCon)
    //val fs = vertx.fileSystem()
    //val afile = fs.openBlocking("index.html", OpenOptions())
    //val gios = ClientGios()
    //println(afile.getReadLength)
    val server = vertx.createHttpServer()
    server.requestHandler(
      router
    ).listen(port, ipHost).onSuccess(server => {
//     ).listen(8989, "192.168.0.120").onSuccess(server => {
        println(s"Server: http://$ipHost:$port")
    }).onFailure(error => {
      println(s"Cannot set server ${error.toString}")
      System.exit(1)
    })

}

