package eu.brosbit

import io.netty.handler.codec.http.HttpResponse
import io.vertx.core.Vertx
import io.vertx.core.streams.Pipe
import io.vertx.core.http.{HttpMethod, HttpServerResponse}
import io.vertx.ext.web.Router

import scala.jdk.CollectionConverters.*
import java.util.Date

class SensorRouter(vertx: Vertx, router:Router) extends RouterBase(vertx, router):
  //val router: Router = Router.router(vertx)

  router.route(HttpMethod.GET, "/measures").handler(ctx =>
    val parTemp: Float = getParamFloat("T", ctx)
    val parTherm = getParam("th", ctx) 
    val parHum = getParamFloat("h", ctx)
    val parSmoke = getParamFloat("s", ctx)
    val parPres = getParamFloat("p", ctx)
    println(s"temperature measure T=$parTemp from $parTherm humidity $parHum smoke $parSmoke")
    val res = ctx.response()
    if parTemp > 0.0f && parTherm.nonEmpty then
      println("Save info for TemperatureData")
      SensorData.addMeasure(parTherm, Date().getTime, parTemp, parHum, parPres, parSmoke)
      res
      .putHeader("content-type", "plain/text")
      .end("OK\n")
    else
      res.setStatusCode(406)
        .putHeader("content-type", "plain/text")
        .end("Wrong parameters\n")
  )

  router.route(HttpMethod.GET, "/boiler").handler(ctx =>
    val T = SensorData.getTemperatureForBoiler
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
    val jsonStr = SensorData.getOwnSettings
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    res.end(jsonStr)
  )
  
  router.route(HttpMethod.GET, "/reload_time_scheduler").handler(ctx =>
    val res = ctx.response()
    SensorData.reloadDataFromFile()
    res.putHeader("content-type", "html/text")
    res.end("ok")
  )
  
  router.route(HttpMethod.GET, "/set_temperature").handler(ctx =>
    val parTemp: Float = getParamFloat("T", ctx)
    val parOn: Boolean = getParamBoolean("on", ctx)

    //println(s"set temperature $parTemp on? $parOn")
    SensorData.setOwnSettings(parTemp, parOn)
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    res.end(SensorData.getOwnSettings)
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




