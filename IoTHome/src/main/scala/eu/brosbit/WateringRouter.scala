package eu.brosbit

import io.vertx.ext.web.Router
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import java.util.Date

class WateringRouter(vertx:Vertx, router:Router) extends RouterBase (vertx, router):
  router.route(HttpMethod.GET, "/getWateringDevices").handler(ctx =>
    val data = WateringData.getWateringDeviceNames
    val res = ctx.response()
    res.setStatusCode(200).putHeader("Content-Type", "application/json").send(data)
  )
  router.route(HttpMethod.GET, "/setWateringInfo").handler( ctx =>
    val device = getParam("d", ctx)
    val res = ctx.response()
    if device.isEmpty then
      res.setStatusCode(404).end()
    else if WateringData.saveWatering(Date().getTime, device) then
      res.setStatusCode(200).putHeader("Content-Type", "plain/text").send("OK")
    else
      res.setStatusCode(500).end()
  )
  router.route(HttpMethod.GET, "/getWateringInfo").handler( ctx =>
    val device = getParam("d", ctx)
    val res = ctx.response()
    if device.isEmpty then
      res.setStatusCode(404).end()
    else
      val data = WateringData.getLastWatering(device)
      res.setStatusCode(200).putHeader("Content-Type", "plain/text").send(data)
  )
  router.route(HttpMethod.GET, "/canWatering").handler( ctx =>
    val device = getParam("d", ctx)
    val res = ctx.response()
    if device.isEmpty then 
      res.setStatusCode(404).end()
    else if WateringData.checkMakeWatering(device) then
      res.setStatusCode(200).send("OK")
    else
      res.setStatusCode(500).end()
  )

  router.route(HttpMethod.GET, "/getMoistureDevices").handler(ctx =>
    val data = WateringData.getMoistureDeviceNames
    val res = ctx.response()
    res.setStatusCode(200).putHeader("Content-Type", "application/json").send(data)
  )

  router.route(HttpMethod.GET, "/getMoistureInfo").handler(ctx =>
    val device = getParam("d", ctx)
    val res = ctx.response()
    if device.isEmpty then
      res.setStatusCode(404).end()
    else
      val data = WateringData.getLastMoisture(device)
      res.setStatusCode(200).putHeader("Content-Type", "plain/text").send(data)
  )
  
  router.route(HttpMethod.GET, "/setMoistureInfo").handler(ctx =>
    val device = getParam("d", ctx)
    val moisture = getParamInt("m", ctx)
    val res = ctx.response()
    if device.isEmpty then
      res.setStatusCode(404).end()
    else if WateringData.saveMoisture(Date().getTime, device, moisture) then
      res.setStatusCode(200).putHeader("Content-Type", "plain/text").send("OK")
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
  createStateRoute("moisture.html")
  createStateRoute("moisture.js")


