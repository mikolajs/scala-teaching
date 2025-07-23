package eu.brosbit

import io.vertx.ext.web.Router
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import scala.jdk.CollectionConverters.*
import java.util.Date

class WateringRouter(vertx:Vertx, router:Router) extends RouterBase (vertx, router):
  router.route(HttpMethod.GET, "/getWateringDevices").handler(ctx =>
    val data = WateringData.getDeviceNames
    val res = ctx.response()
    res.setStatusCode(200).putHeader("Content-Type", "application/json").send(data)
  )
  router.route(HttpMethod.GET, "/setWateringInfo").handler( ctx =>
    val device = ctx.queryParam("d").asScala.toList match {
      case Nil => ""
      case d::rest => d.trim
    }
    val res = ctx.response()
    if device.isEmpty then
      res.setStatusCode(404).end()
    else if WateringData.saveWatering(Date().getTime, device) then
      res.setStatusCode(200).putHeader("Content-Type", "plain/text").send("OK")
    else
      res.setStatusCode(500).end()
  )
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
  createStateRoute("watering.html")
  createStateRoute("watering.js")


