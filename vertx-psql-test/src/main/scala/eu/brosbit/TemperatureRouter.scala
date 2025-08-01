package eu.brosbit

import io.netty.handler.codec.http.HttpResponse
import io.vertx.core.Vertx
import io.vertx.core.streams.Pipe
import io.vertx.core.http.{HttpMethod, HttpServerResponse}
import io.vertx.ext.web.Router

import scala.jdk.CollectionConverters.*
import java.util.Date

class TemperatureRouter(vertx: Vertx, router:Router, dbCon:DBConnect) extends RouterBase(vertx, router):
  private val tempData = TemperatureData()
  //set temperature from measure device
  router.route(HttpMethod.GET, "/temp").handler(ctx =>
    val parTemp: Float = getParamFloat("T", ctx)
    val parTherm = getParamStr("th", ctx)
    val parHum = getParamFloat("h", ctx)
    //println(s"temperature measure T=$parTemp from $parTherm humidity $parHum")
    val res = ctx.response()
    if parTemp > 0.0f && parTherm.nonEmpty then
//      println("Save info for TemperatureData")
      val client = dbCon.client
      val time = Date().getTime
      tempData.addMeasure(parTherm, time, parTemp)
      val query = s"insert into measure  (th, time, T) values ('$parTherm', $time, $parTemp)"
      client.query(query).execute()
        .onComplete(ar =>
          if ar.succeeded() then foundData(res).end()
          else notFoundData(res).end()
        ).onFailure(e => MainLogger.infoLog("Error insert measure \n" + e.getMessage))
    else
      res.setStatusCode(406).putHeader("content-type", "plain/text").end("Wrong parameters\n")
  )
  //check expected temperature for boiler
  router.route(HttpMethod.GET, "/boiler").handler(ctx =>
    val T = tempData.getTemperatureForBoiler
    val aTime = Date().getTime 
    val client = dbCon.client
    val query = s"insert into boiler_set_temperature values ($aTime, $T)"
    client.query(query).execute()
      .onComplete(ar =>
        if !ar.succeeded() then MainLogger.infoLog("Temperature set boiled NOT succeed")
      ).onFailure(e => MainLogger.infoLog("Error set boiler temp\n"+e.getMessage))
    val airT = tempData.getExpectedTemperatureOfAir
    val query2 = s"insert into expected_temperature values ($aTime, $airT)"
    client.query(query).execute()
      .onComplete(ar =>
        if !ar.succeeded() then MainLogger.infoLog("Set expected air temperature FAILURE")
      ).onFailure(e => MainLogger.infoLog("Error set boiler temp\n" + e.getMessage))

    val res = ctx.response()
    //println(s"Set temperature boiler: $T")
    MainLogger.infoLog(s"Expected temperature for boiler: $T")
    res.putHeader("content-type", "plain/text").end(T.toString)
  )

  router.route(HttpMethod.GET, "/addboilerinfo").handler(ctx =>
    val returnTemp = getParamFloat("rwt", ctx)
    val boilerTemp = getParamFloat("bwt", ctx)
    val setpointBounds = getParamFloat("spb", ctx)
    val oemDiagnostic = getParamInt("oemd", ctx)
    val t = Date().getTime
    val res = ctx.response()
    val query = s"""insert into boiler_info (time, return_temperature, boiler_temperature, setpoint_bound, oem_diagnostic) values
      | ($t, $returnTemp, $boilerTemp, $setpointBounds, $oemDiagnostic)
             """.stripMargin
    val client = dbCon.client
    client.query(query).execute()
      .onComplete(ar =>
        if ar.succeeded() then foundData(res).end("OK")
        else notFoundData(res).end()
      ).onFailure(e => MainLogger.infoLog("Error adding boiler info \n"+e.getMessage))
  )

  router.route(HttpMethod.GET, "/info/expected_temperature").handler(ctx =>
    val query = s"select time, T from expected_temperature order by time desc limit 120"
    val res = ctx.response()
    val client = dbCon.client
    client.query(query).execute()
      .onComplete(ar => 
        if ar.succeeded() then
          val rows = ar.result()
          val boilerData = DataCreator.createJsonArray(rows, "boiler", 2)
          foundData(res).end(boilerData)
        else notFoundData(res).end("Not found")
      ).onFailure(e => MainLogger.infoLog("Error reading expected temperature of boiler\n"+e.getMessage)
      )
  )

  router.route(HttpMethod.GET, "/info/measures").handler( ctx =>
    val res = ctx.response()
    val query = "select th, time, T from measure order by time desc limit 120"
    val client = dbCon.client
    client.query(query).execute()
      .onComplete(ar =>
        if ar.succeeded() then
          val rows = ar.result()
          val data = DataCreator.createJsonArray(rows, "measures", 3)
          foundData(res).end(data)
        else notFoundData(res).end()
      ).onFailure(e => MainLogger.infoLog("Error info measures\n"+e.getMessage))
  )

  router.route(HttpMethod.GET, "/info/boiler_info").handler(ctx =>
    val res = ctx.response()
    val query = """select time, return_temperature, boiler_temperature, setpoint_bound, oem_diagnostic from boiler_info
                  | order by time desc limit 120""".stripMargin
    val client = dbCon.client
    client.query(query).execute()
      .onComplete(ar =>
        if ar.succeeded() then
          val rows = ar.result()
          val data = DataCreator.createJsonArray(rows, "boiler", 5)
          foundData(res).end(data)
        else notFoundData(res).end()
      ).onFailure(e => MainLogger.infoLog("Error boiler info\n"+e.getMessage))
  )

  router.route(HttpMethod.GET, "/info/boiler_set_temp").handler(ctx =>
    val query = s"select time, T from boiler_set_temperature order by time desc limit 120"
    val res = ctx.response()
    dbCon.client.query(query).execute()
      .onComplete(ar =>
        if ar.succeeded() then
          val rows = ar.result()
          val data = DataCreator.createJsonArray(rows, "boiler", 2)
          foundData(res).end(data)
        else notFoundData(res).end()
      ).onFailure(e => MainLogger.infoLog("Error boiler set temperature\n"+e.getMessage))
  )

  router.route(HttpMethod.GET, "/info/temp_setting").handler(ctx =>
    val jsonStr = tempData.getOwnSettings
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    res.end(jsonStr)
  )

  router.route(HttpMethod.GET, "/reload_time_scheduler").handler(ctx =>
    val res = ctx.response()
    tempData.reloadDataFromFile()
    res.putHeader("content-type", "html/text")
    res.end("ok")
  )

  //ustwienie własnej temperatury ze strony. 
  router.route(HttpMethod.GET, "/set_temperature").handler(ctx =>
    val parTemp: Float = getParamFloat("T", ctx)
    val parOn: Boolean = try ctx.queryParam("on").asScala.head.trim.toBoolean
      catch
        case e => false

    //println(s"set temperature $parTemp on? $parOn")
    tempData.setOwnSettings(parTemp, parOn)
    val res = ctx.response()
    res.putHeader("content-type", "application/json")
    res.end(tempData.getOwnSettings)
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




