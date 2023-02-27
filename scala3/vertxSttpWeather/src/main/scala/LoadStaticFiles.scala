package eu.brosbit

import io.vertx.core.file.FileSystem
import io.vertx.core.http.{HttpServerRequest, HttpServerResponse}
import io.vertx.core.buffer.Buffer

class LoadStaticFiles(fs:FileSystem):

  def staticFile(res:HttpServerResponse, name:String) =
    var fileData = ""
    fs.readFile("files/"+name, (result) => {
      if result.succeeded() then sendRes(res, name, result.result().toString("utf-8"))
      else sendFail(res, name)
    })

  private def sendRes(res:HttpServerResponse, name:String, fileData:String) =
    res.putHeader("Content-Type", checkType(name))
      .putHeader("Access-Control-Allow-Origin", "*")
      .end(fileData)

  private def sendFail(res:HttpServerResponse, name:String) =
    res.setStatusCode(404)
      .putHeader("Content-Type", "text/json")
      .putHeader("Access-Control-Allow-Origin", "*")
      .end(s"{'Error':'file: $name not found'}")

  private def checkType(name:String) =
    name.toLowerCase().split('.').last match
      case "js" => "text/javascript"
      case "html" => "text/html"
      case "css" => "text/css"
      case "png" => "image/png"
      case "gif" => "image/gif"
      case "jpg" | "jpeg" => "image/jpg"
      case _ => "text/plain"

