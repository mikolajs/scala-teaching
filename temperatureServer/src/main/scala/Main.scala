package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import io.vertx.core.http.HttpServerRequest

import scala.collection.mutable
import scala.io.StdIn.readLine

object Main {
  //val temp:Array[Double] = Array[Double](10).map(_ => Double.NaN)
  val temp:Array[Double] = Array.ofDim[Double](10)
  val hum:Array[Double] = Array.ofDim[Double](10)

  def main(args:Array[String]):Unit =
    val vertx = Vertx.vertx()
    val fs = vertx.fileSystem()
    var indexHTML = ""
    val aFile = fs.openBlocking("index.html", OpenOptions()).handler(aFileBuff => {
      indexHTML = aFileBuff.toString("utf-8")
    })
    aFile.endHandler(_ =>
      aFile.close()
    )
    val loadStaticFiles = new LoadStaticFiles(fs)
    vertx.createHttpServer().requestHandler(req =>  {
      println(req.path())
      println(req.params())
      if req.path() == "/" || req.path() == "/index" then req.response()
        .putHeader("content-type", "text/html")
        .putHeader("Access-Control-Allow-Orgin", "*")
        .end(indexHTML)
      else if req.path().split('/')(1) == "files" then
        loadStaticFiles.staticFile(req.response(), req.path().split('/').last)
      else if req.path() == "/get" then req.response()
        .putHeader("content-type", "application/json")
        .putHeader("Access-Control-Allow-Origin", "*")
        .end(getTemps)
      else if req.path().split('/')(1) == "set" then readTem(req)
      else req.response()
        .putHeader("content-type", "application/json")
        .putHeader("Access-Control-Allow-Origin", "*")
        .end("{'Error':'Not found!'}")
    }).listen(9900, "192.168.0.104").onSuccess(server => {
      println("Server: http://192.168.0.104:" + server.actualPort())})

  def getTemps =
    val tms = temp.filter(!_.isNaN)
    val ts = for i <- 0 until tms.size yield "{\"name\": \""+ i.toString + "\",\"temp\":"+tms(i)+"}"
    s"""{"measures":[${ts.mkString(",")}]}"""

  def readTem(req: HttpServerRequest) =
    println("READ TEMPERATURES")
    val t = req.params().get("t")
    val h = req.params().get("h")
    val nr = req.path().split('/').last.toInt
    val res = req.response()
      .putHeader("content-type", "text/html")
      .putHeader("Access-Control-Allow-Orgin", "*")
    if nr > 0 && nr < 10 then
      temp(nr) = t.toDouble
      hum(nr) = h.toDouble
      res.end("OK")
    else
      res.setStatusCode(400)
        .end("ERROR")


}

