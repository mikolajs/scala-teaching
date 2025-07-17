package eu.brosbit

import scala.jdk.CollectionConverters.*
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod

class PZEMRouter(vertx: Vertx, router: Router) extends RouterBase(vertx, router):
  router.route(HttpMethod.GET, "/addpowerinfo").handler(ctx =>
    val measurerName: String = ctx.queryParam("m").asScala.toList match
      case Nil => ""
      case m :: rest => m.trim

    val voltage:Float = ctx.queryParam("v").asScala.toList match
      case Nil => 0.0
      case v::rest => try v.trim.toFloat catch case e => 0.0

    val current: Float = ctx.queryParam("a").asScala.toList match
      case Nil => 0.0
      case v :: rest => try v.trim.toFloat catch case e => 0.0

    val power: Float = ctx.queryParam("p").asScala.toList match
      case Nil => 0.0
      case v :: rest => try v.trim.toFloat catch case e => 0.0

    val energy: Float = ctx.queryParam("e").asScala.toList match
      case Nil => 0.0
      case v :: rest => try v.trim.toFloat catch case e => 0.0

    val frequency: Float = ctx.queryParam("f").asScala.toList match
      case Nil => 0.0
      case v :: rest => try v.trim.toFloat catch case e => 0.0

    val pf: Float = ctx.queryParam("x").asScala.toList match
      case Nil => 0.0
      case v :: rest => try v.trim.toFloat catch case e => 0.0
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
    val pzemName = ctx.queryParam("n").asScala.toList match {
      case Nil => ""
      case n::rest => n.trim
    }
    val jsonStr = PZEMData.getMeasures(pzemName)
    val res = ctx.response()
    res.setStatusCode(200)
    res.putHeader("content-type", "application/json").send(jsonStr)
  )

  createStateRoute("pzemmeasures.html")
  createStateRoute("pzemmeasures.js")


