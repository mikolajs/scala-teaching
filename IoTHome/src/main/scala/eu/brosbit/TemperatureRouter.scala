package eu.brosbit

import io.netty.handler.codec.http.HttpResponse
import io.vertx.core.Vertx
import io.vertx.core.streams.Pipe
import io.vertx.core.http.{HttpMethod, HttpServerResponse}
import io.vertx.ext.web.Router

import scala.jdk.CollectionConverters.*
import java.util.Date

class TemperatureRouter(vertx:Vertx):
  val router: Router = Router.router(vertx)

  router.route(HttpMethod.GET, "/temp").handler(ctx =>
    val parTemp: Float =
      try ctx.queryParam("T").asScala.head.trim.toFloat
      catch
        case e => 0.0f
    val parTherm = 
      try ctx.queryParam("th").asScala.head.trim
      catch
        case e => ""
    val parHum =
      try ctx.queryParam("h").asScala.head.trim
      catch
        case e => 0.0f
    println(s"temperature measure T=$parTemp from $parTherm humidity $parHum")
    val res = ctx.response()
    if parTemp > 0.0f && parTherm.nonEmpty then
      println("Save info for TemperatureData")
      TemperatureData.addMeasure(parTherm.head, Date().getTime, parTemp)
      res
      .putHeader("content-type", "plain/text")
      .end("OK\n")
    else
      res.setStatusCode(406)
        .putHeader("content-type", "plain/text")
        .end("Wrong parameters\n")
  )

  router.route(HttpMethod.GET, "/boiler").handler(ctx =>
    val T = TemperatureData.getTemperatureForBoiler
    val res = ctx.response()
    println(s"Set temperature boiler: $T")
    res.putHeader("content-type", "plain/text").end(T.toString)
  )

  router.route(HttpMethod.GET, "/addboilerinfo").handler(ctx =>
    val returnWaterTemp = try ctx.queryParam("rwt").asScala.head.trim.toFloat
    catch
      case _ => -1.0f

    val boilerWaterTemp = try ctx.queryParam("bwt").asScala.head.trim.toFloat
    catch
      case _ => -1.0f

    val setpointBounds = try ctx.queryParam("spb").asScala.head.trim.toFloat
    catch
      case _ => -1.0f

    val oemDiagnostic = try ctx.queryParam("oemd").asScala.head.trim.toInt
    catch
      case _ => -1

    //if boilerWaterTemp > 20 then TemperatureData.setBoilerTemperature(boilerWaterTemp)
    DBConnect.insertBoilerInfo(Date().getTime, returnWaterTemp, boilerWaterTemp, setpointBounds, oemDiagnostic)
    val res = ctx.response()
    res.end("OK")
  )

  router.route(HttpMethod.GET, "/info/expected_temperature").handler(ctx =>
    val boilers = DBConnect.selectLastBoilerExpectedTemperature()
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    //res.putHeader("Access-Control-Allow-Origin:", "*")
    //res.putHeader("Accept:", "text/html,application/json;q=0.9,*/*;q=0.8")
    res.end("{\"boiler\":[" + boilers.map(_.toJson).mkString(",") + "]}\n")
  )

  router.route(HttpMethod.GET, "/info/measures").handler( ctx =>
    val measures = DBConnect.selectLastMeasures()
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    //res.putHeader("Access-Control-Allow-Origin:", "*")
    //res.putHeader("Accept:", "text/html,application/json;q=0.9,*/*;q=0.8")
    res.end("{\"measures\":[" + measures.map(_.toJson).mkString(",") + "]}\n")
  )

  router.route(HttpMethod.GET, "/info/boiler_info").handler(ctx =>
    val boilers = DBConnect.selectLastBoilerInfo()
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    //res.putHeader("Access-Control-Allow-Origin:", "*")
  //res.putHeader("Accept:", "text/html,application/json;q=0.9,*/*;q=0.8")
    res.end("{\"boiler\":[" + boilers.map(_.toJson).mkString(",") + "]}\n")
  )

  router.route(HttpMethod.GET, "/info/boiler_set_temp").handler(ctx =>
    val boilerInfoList = DBConnect.selectBoilerSetTemperature()
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    res.end("{\"boiler\":[" + boilerInfoList.map(_.toJson).mkString(", ") + "]}")
  )
  
  router.route(HttpMethod.GET, "/info/temp_setting").handler(ctx =>
    val jsonStr = TemperatureData.getOwnSettings
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    res.end(jsonStr)
  )
  
  router.route(HttpMethod.GET, "/set_temperature").handler(ctx =>
    val parTemp: Float =
      try ctx.queryParam("T").asScala.head.trim.toFloat
      catch
        case e => 0.0f
    val parOn: Boolean =
      try ctx.queryParam("on").asScala.head.trim.toBoolean
      catch
        case e => false

    //println(s"set temperature $parTemp on? $parOn")
    TemperatureData.setOwnSettings(parTemp, parOn)
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    res.end(TemperatureData.getOwnSettings)
  )
  
  router.route(HttpMethod.GET, "/").handler(ctx =>{
    val res = ctx.response()
    //println("Start / page")
    vertx.fileSystem().readFile("index.html").onSuccess(file =>{
      val data = file.toString("UTF-8")
      res.end(data)
    }).onFailure(e =>{
       res.setStatusCode(406)
       res.end("error\n")
    })
  })

  createStateRoute("chart-utils.min.js")
  createStateRoute("iothome-chart.js")
  createStateRoute("boilerstate.html")
  createStateRoute("test.html")
  createStateRoute("own_temp.html")
  createStateRoute("test.js")
  createPictureRoute("favicon.ico")

  private def createStateRoute(path:String):Unit =
    val arr = path.split('.')
    val fullPath = if arr.last != "html" then "/"+path else "/"+arr.dropRight(1).mkString(".")
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
    
  private def createPictureRoute(path:String):Unit =
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
    
  private def insertTypeString(path:String, res:HttpServerResponse):Unit =
    path.split('.').last match
      case ext if ext == "js" => res.putHeader("content-type", "text/javascript")
      case ext if ext == "json" =>res.putHeader("content-type", "application/json")
      case ext if ext == "ico" => res.putHeader("content-type", "image/x-icon")
      case ext if ext == "png" => res.putHeader("content/type", "image/png")
      case _ =>


