package eu.brosbit

import scala.jdk.CollectionConverters.*
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod

class PZEMRouter(vertx: Vertx, router: Router) extends RouterBase(vertx, router):
  router.route(HttpMethod.GET, "/addpowerinfo").handler(ctx =>
    val measurerName: String = getParam("m", ctx)
    val voltage:Float = getParamFloat("v", ctx)
    val current: Float = getParamFloat("a", ctx)
    val power: Float = getParamFloat("p", ctx)
    val energy: Float = getParamFloat("e", ctx)
    val frequency: Float = getParamFloat("f", ctx)
    val pf: Float = getParamFloat("x", ctx)
    //println(s"$measurerName, $voltage, $current, $power, $energy, $frequency, $pf")
    val res = ctx.response()
    PZEMData.saveMeasure(measurerName, voltage, current, power, energy, frequency, pf)
    res.putHeader("conent-type", "plain/text")
  )

  router.route(HttpMethod.GET, "/getPzemNames").handler(ctx =>
    val jsonStr = PZEMData.getNames
//    println(jsonStr)
    val res = ctx.response()
    res.setStatusCode(200)
    res.putHeader("content-type", "application/json").send(jsonStr)
  )

  router.route(HttpMethod.GET, "/getPzemMeasures").handler(ctx =>
    val pzemName = getParam("n", ctx)
    val jsonStr = PZEMData.getMeasures(pzemName)
    val res = ctx.response()
    res.setStatusCode(200)
    res.putHeader("content-type", "application/json").send(jsonStr)
  )

  createStateRoute("pzemmeasures.html")
  createStateRoute("pzemmeasures.js")


