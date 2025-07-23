package eu.brosbit

import io.vertx.ext.web.Router
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import scala.jdk.CollectionConverters.*
import java.util.{Calendar, Date, GregorianCalendar}

class WateringRouter(vertx:Vertx, router:Router, dbCon:DBConnect) extends RouterBase (vertx, router):
  val help = WateringHelp()
  router.route(HttpMethod.GET, "/getWateringDevices").handler(ctx =>

    println("show watering devices ")
    val res = ctx.response()
    dbCon.printInfo()
    println("printed?")
    val client = dbCon.client
    client.query("select distinct device from watering_info").execute()
      .onComplete(ar =>
        println("completed select water_info " + ar.succeeded().toString)
        if ar.succeeded() then {
          val rows = ar.result()
          var devices:List[String] = Nil
          rows.forEach(row =>
            devices = row.getString(0)::devices
          )
          foundData(res)
          res.send(help.devicesNamesToJson(devices))
        } else
          println("error db")
          res.setStatusCode(404).putHeader("Content-Type", "plain/text").end()
      ).onFailure( e =>
        e.printStackTrace()
      )
  )

  router.route(HttpMethod.GET, "/setWateringInfo").handler( ctx =>
    val device = ctx.queryParam("d").asScala.toList match {
      case Nil => ""
      case d::rest => d.trim
    }
    val res = ctx.response()
    if device.isEmpty then {
      notFoundResource(res)
      res.end()
    } else {
      val client = dbCon.client
      val date = Date().getTime
      val query = s"insert into watering_info values ($date, '$device')"
      client.query(query).execute()
        .onComplete(ar =>
          MainLogger.infoLog("completed insert water_info " + ar.succeeded().toString)
          if ar.succeeded() then {
            foundData(res)
            res.end()
          } else
            println("error db")
            notFoundResource(res)
            res.end()
        ).onFailure(e =>
          println(e.getMessage)
          notFoundResource(res)
          res.end()
        )
    }
  )
  /*
  router.route(HttpMethod.GET, "/getWateringInfo").handler( ctx =>
    val device = ctx.queryParam("d").asScala.toList match
      case Nil => ""
      case d::rest => d.trim

    val res = ctx.response()
    if device.isEmpty then 
      res.setStatusCode(404).end()
    else
      val data = WateringData.getLastWatering(device)
      res.setStatusCode(200).putHeader("Content-Type", "plain/text").send(data)
  )
  router.route(HttpMethod.GET, "/canWatering").handler( ctx =>
    val device = ctx.queryParam("d").asScala.toList match
      case Nil => ""
      case d::rest => d.trim
    
    val res = ctx.response()
    if device.isEmpty then 
      res.setStatusCode(404).end()
    else if WateringData.checkMakeWatering(device) then
      res.setStatusCode(200).send("OK")
    else
      res.setStatusCode(500).end()
  )
  router.route(HttpMethod.GET, "/reloadWateringScheduler").handler(ctx =>
    WateringData.reloadScheduler()
    val res = ctx.response()
    res.setStatusCode(200).putHeader("Content-Type", "plain/text").end()
  )
  */
  createStateRoute("watering.html")
  createStateRoute("watering.js")


