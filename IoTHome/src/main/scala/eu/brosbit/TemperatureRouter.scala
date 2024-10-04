package eu.brosbit

import io.netty.handler.codec.http.HttpResponse
import io.vertx.core.Vertx
import io.vertx.core.streams.Pipe
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import scala.jdk.CollectionConverters._
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

  router.route(HttpMethod.GET, "/info/measures").handler( ctx =>
    val measures = DBConnect.checkLastMeasures()
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    //res.putHeader("Access-Control-Allow-Origin:", "*")
    //res.putHeader("Accept:", "text/html,application/json;q=0.9,*/*;q=0.8")
    res.end("{\"measures\":[" + measures.map(_.toJson).mkString(",") + "]}\n")
  )

  router.route(HttpMethod.GET, "/info/boilerset").handler(ctx =>
    val boilers = DBConnect.checkLastBoilerSet()
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    //res.putHeader("Access-Control-Allow-Origin:", "*")
  //res.putHeader("Accept:", "text/html,application/json;q=0.9,*/*;q=0.8")
    res.end("{\"boiler\":[" + boilers.map(_.toJson).mkString(",") + "]}\n")
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
    DBConnect.mkInsertBoilerInfo(Date().getTime, returnWaterTemp, boilerWaterTemp, setpointBounds, oemDiagnostic)
    val res = ctx.response()
    res.end("OK")
  )

  router.route(HttpMethod.GET, "/info/boiler").handler(ctx =>
    val boilerInfoList = DBConnect.checkBoilerInfo()
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    res.end("{\"boiler\":[" + boilerInfoList.map(_.toJson).mkString(", ") + "]}")
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

  router.route(HttpMethod.GET, "/boilerstate").handler(ctx =>{
    val res = ctx.response()
    //println("Start / page")
    vertx.fileSystem().readFile("boiler.html").onSuccess(file =>{
      val data = file.toString("UTF-8")
      res.end(data)
    }).onFailure(e =>{
      res.setStatusCode(406)
      res.end("error\n")
    })
  })


