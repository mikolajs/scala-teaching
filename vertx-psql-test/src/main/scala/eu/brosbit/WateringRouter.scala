package eu.brosbit

import io.vertx.ext.web.Router
import io.vertx.core.Vertx
import io.vertx.core.http.{HttpMethod, HttpServerResponse}
import scala.jdk.CollectionConverters.*
import java.util.{Calendar, Date, GregorianCalendar}



class WateringRouter(vertx:Vertx, router:Router, dbCon:DBConnect) extends RouterBase (vertx, router):
  private var wateringMap: Map[String, (Int, Int)] = loadExpectedTempScheduler

  router.route(HttpMethod.GET, "/getWateringDevices").handler(ctx =>
    //println("show watering devices ")
    val res = ctx.response()
    dbCon.printInfo()
    //println("printed?")
    val client = dbCon.client
    client.query("select distinct device from watering_info").execute()
      .onComplete(ar =>
        //println("completed select water_info " + ar.succeeded().toString)
        if ar.succeeded() then {
          val rows = ar.result()
          val devices:List[String] = DataCreator.createStringArray(rows)
          foundData(res)
          res.send(devicesNamesToJson(devices))
        } else {
          //println("error db")
          MainLogger.infoLog("Cannot find watering devices")
          res.setStatusCode(404).putHeader("Content-Type", "plain/text").end()
        }
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
    MainLogger.infoLog(s"device $device inform about watering ")
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
            MainLogger.infoLog(s"Cannot find last watering data for $device")
            notFoundResource(res)
            res.end()
        ).onFailure(e =>
          MainLogger.infoLog(e.getMessage)
          notFoundResource(res)
          res.end()
        )
    }
  )
  router.route(HttpMethod.GET, "/getWateringInfo").handler( ctx =>
    val device = ctx.queryParam("d").asScala.toList match
      case Nil => ""
      case d::rest => d.trim

    val res = ctx.response()
    if device.isEmpty then {
      notFoundResource(res)
      res.end()
    } else
      val client = dbCon.client
      val query = s"select watering_time from watering_info where device = '$device' order by watering_time DESC"
      client.query(query).execute()
        .onComplete(ar =>
          //println(s"completed select watering for $device " + ar.succeeded().toString)
          if ar.succeeded() then {
            val rows = ar.result()
            val data:List[Long] = DataCreator.createLongArray(rows)
            foundData(res)
            res.send(s"""{"wateringTimes":[${data.mkString(",")}]} """)
          } else {
            notFoundResource(res)
            res.end()
          }
        ).onFailure(e =>
          println(e.getMessage)
        )
  )

  router.route(HttpMethod.GET, "/canWatering").handler( ctx =>
    val device = ctx.queryParam("d").asScala.toList match
      case Nil => ""
      case d::rest => d.trim
    
    val res = ctx.response()
    if device.isEmpty then {
      notFoundResource(res)
      res.end()
    } else checkMakeWatering(device, res)
  )

  router.route(HttpMethod.GET, "/reloadWateringScheduler").handler(ctx =>
    reloadScheduler()
    val res = ctx.response()
    res.setStatusCode(200).putHeader("Content-Type", "plain/text").end()
  )

  createStateRoute("watering.html")
  createStateRoute("watering.js")

  private def checkMakeWatering(device: String, res:HttpServerResponse):Unit =
    val client = dbCon.client
    val query = s"select watering_time from watering_info where device = '$device' order by watering_time DESC limit 1"
    println(query)
    client.query(query).execute()
      .onComplete(ar =>
        if ar.succeeded() then {
          val rows = ar.result()
          if rows.rowCount() < 1 then {
            //println("rows to small: "+ rows.rowCount())
            res.setStatusCode(200).end("OK")
          } else {
            val lastTime = rows.iterator().next().getLong(0)
            //println("rows OK: " + rows.rowCount() + " last time: " + lastTime)
            if checkShutWatering(lastTime, device) then {
              MainLogger.infoLog(s" device $device take confirm watering time")
              res.setStatusCode(200).end("OK")
            } else
              MainLogger.infoLog(s" device $device NOT take confirm watering time")
              res.setStatusCode(400).end("not")
          }
        } else
          res.setStatusCode(200).end("OK")
      ).onFailure( e =>
        MainLogger.infoLog(e.getMessage)
      )

  private def checkShutWatering(lastWatering:Long, device:String):Boolean =
    val (wateringDistance, wateringHour) = if wateringMap.contains(device) then wateringMap(device) else (-1, -1)
    val gc = GregorianCalendar()
    val now = gc.getTime.getTime
    val distance: Long = (now - lastWatering) / 1000L  //seconds (getTime in milliseconds)
    //println(s"device $device $lastWatering $wateringHour ${gc.getTime.toString} distance: $distance")
    MainLogger.infoLog(s"device: $device, time not watering: $distance")
    MainLogger.infoLog(s"hour to $wateringDistance, start hour $wateringHour")
    if lastWatering <= 0L && wateringHour < 0 then true
    else if wateringHour < 0 then
      if distance >= wateringDistance * 3600L then true
      else false
    else
      val distance: Long = (now - lastWatering) / 1000L
      val hourNow = gc.get(Calendar.HOUR_OF_DAY)
      if hourNow >= wateringHour && distance > wateringDistance * 3600L then true
      else false

  private def devicesNamesToJson(devices: List[String]): String =
    val content = devices.map(d =>
      s"\"$d\""
    ).mkString(",")
    s"""{"devices":[$content]}"""


  private def reloadScheduler(): Unit = {
    MainLogger.infoLog("reloaded Watering Scheduler")
    //println("Reloaded Watering Scheduler")
    wateringMap = loadExpectedTempScheduler
  }

  private def loadExpectedTempScheduler: Map[String, (Int, Int)] =
    val source = scala.io.Source.fromFile(s"/etc/iothome/watering.cfg")
    val map = source.getLines.toList.map(line =>
        if line.trim.head == '#' then List("", "")
        else line.trim.split(" ").filter(_.trim.nonEmpty).toList.take(3)
      ).filter(l => l.length == 3).filterNot(l => l.head.isEmpty)
      .map(l => (l.head, (l(1).toInt, l(2).toInt))).toMap
    source.close()
    map