package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import scala.collection.mutable
import scala.io.StdIn.readLine

object Main {
  //val temp:Array[Double] = Array[Double](10).map(_ => Double.NaN)
  val temp:Array[Double] = Array.ofDim[Double](10).map(_ => Math.random()*30)

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
      else req.response()
        .putHeader("content-type", "application/json")
        .putHeader("Access-Control-Allow-Origin", "*")
        .end("{'Error':'Not found!'}")
    }).listen(9900, "localhost").onSuccess(server => {
      println("Server: http://localhost:" + server.actualPort())})

  def getTemps =
    val tms = temp.filter(!_.isNaN)
    val ts = for i <- 0 until tms.size yield "{\"name\": \"m"+ i.toString + "\",\"temp\":"+tms(i)+"}"
    s"""{"measures":[${ts.mkString(",")}]}"""

}

