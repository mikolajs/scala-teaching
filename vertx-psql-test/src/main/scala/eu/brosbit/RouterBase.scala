package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.core.http.{HttpMethod, HttpServerResponse}
import io.vertx.ext.web.Router

class RouterBase(vertx:Vertx, router: Router):
  protected def createStateRoute(path: String): Unit =
    val arr = path.split('.')
    val fullPath = if arr.last != "html" then "/" + path else "/" + arr.dropRight(1).mkString(".")
    println(fullPath)
    router.route(HttpMethod.GET, fullPath).handler(ctx => {
      val res = ctx.response()
      vertx.fileSystem().readFile(path).onSuccess(file => {
        val data = file.toString("UTF-8")
        insertTypeString(path, res)
        res.end(data)
      }).onFailure(e => {
        res.setStatusCode(404)
        res.end("error\n")
      })
    })

  protected def createPictureRoute(path: String): Unit =
    router.route(HttpMethod.GET, "/" + path).handler(ctx => {
      val res = ctx.response()
      vertx.fileSystem().readFile(path).onSuccess(file => {
        val data = file
        insertTypeString(path, res)
        res.end(data)
      }).onFailure(e => {
        res.setStatusCode(404)
        res.end("error\n")
      })
    })
    
  protected def notFoundData(res:HttpServerResponse):Unit = 
    res.setStatusCode(204).putHeader("Content-Type", "plain/text")

  protected def foundData(res:HttpServerResponse):Unit =
    res.setStatusCode(200).putHeader("Content-Type", "application/json")
    
  protected def notFoundResource(res:HttpServerResponse):Unit =
    res.setStatusCode(404).putHeader("Content-Type", "plain/text")
  
  private def insertTypeString(path: String, res: HttpServerResponse): Unit =
    path.split('.').last match
      case ext if ext == "js" => res.putHeader("content-type", "text/javascript")
      case ext if ext == "json" => res.putHeader("content-type", "application/json")
      case ext if ext == "ico" => res.putHeader("content-type", "image/x-icon")
      case ext if ext == "png" => res.putHeader("content/type", "image/png")
      case _ =>
