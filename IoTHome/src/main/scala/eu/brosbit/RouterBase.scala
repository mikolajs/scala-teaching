package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.core.http.{HttpMethod, HttpServerResponse}
import io.vertx.ext.web.{Router, RoutingContext}
import scala.jdk.CollectionConverters.*

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
        println("picture: " + path)
        res.end(data)
      }).onFailure(e => {
        res.setStatusCode(404)
        res.end("error\n")
      })
    })
    
  protected def getParam(name:String, ctx:RoutingContext):String = {
    val p = ctx.queryParam(name)
    if !p.isEmpty then p.asScala.head.trim else ""
  }
  
  protected def getParamInt(name:String, ctx:RoutingContext):Int =
    val p = getParam(name, ctx)
    if p.nonEmpty && p.forall(c => c.isDigit) then p.toInt
    else 0

  protected def getParamFloat(name: String, ctx: RoutingContext): Float =
    val p = getParam(name, ctx)
    if p.nonEmpty && p.split('.').mkString.forall(c => c.isDigit) then p.toFloat
    else 0.0f
    
  protected def getParamBoolean(name: String, ctx: RoutingContext): Boolean =
    val p = getParam(name, ctx)
    if p.nonEmpty && (p == "true" || p == "false") then p.toBoolean else false

  private def insertTypeString(path: String, res: HttpServerResponse): Unit =
    path.split('.').last match
      case ext if ext == "js" => res.putHeader("content-type", "text/javascript")
      case ext if ext == "json" => res.putHeader("content-type", "application/json")
      case ext if ext == "ico" => res.putHeader("content-type", "image/x-icon")
      case ext if ext == "png" => res.putHeader("content-type", "image/png")
      case _ =>
