package eu.brosbit

import scala.jdk.CollectionConverters.*
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.core.http.{HttpMethod, HttpServerResponse}
import java.util.Date

class PZEMRouter(vertx: Vertx, router: Router, dbCon: DBConnect) extends RouterBase(vertx, router):
  router.route(HttpMethod.GET, "/addpowerinfo").handler(ctx =>
    val measurerName: String = getParamStr("m", ctx)
    val voltage:Float = getParamFloat("v", ctx)
    val current: Float = getParamFloat("a", ctx)
    val power: Float = getParamFloat("p", ctx)
    val energy: Float = getParamFloat("e", ctx)
    val frequency: Float = getParamFloat("f", ctx)
    val pf: Float = getParamFloat("x", ctx)
    //println(s"$measurerName, $voltage, $current, $power, $energy, $frequency, $pf")
    val res = ctx.response()
    mkSaveMeasure(measurerName, voltage, current, power, energy, frequency, pf, res)
  )

  router.route(HttpMethod.GET, "/getPzemNames").handler(ctx =>
    val client = dbCon.client
    val query = s"select distinct measurer from pzem_info"
    val res = ctx.response()
    client.query(query).execute()
      .onComplete(ar =>
        if ar.succeeded() then
          val rows = ar.result()
          val dataList = DataCreator.createStringArray(rows)
          val dataJsonStr = dataList.map(n => s""" "$n" """).mkString(",")
          val data = """{"pzem_names":[ """ + dataJsonStr + "]}"
          foundData(res).send(data)
        else
          notFoundData(res).end()
      ).onFailure(e => MainLogger.infoLog("Cannot find measures \n" + e.getMessage))
  )

  router.route(HttpMethod.GET, "/getPzemMeasures").handler(ctx =>
    val pzemName = getParamStr("n", ctx)
    println(s"check pzem measures $pzemName")
    val res = ctx.response()
    if pzemName.isEmpty then notFoundData(res).end()
    else
      val client = dbCon.client
      val query = s"select * from pzem_info where measurer = '$pzemName' order by create_time DESC limit 120"
      client.query(query).execute()
        .onComplete(ar =>
          if ar.succeeded() then
            println("PZEM get measures succeed")
            val rows = ar.result()
            val jsonStr = DataCreator.createJsonArray(rows, "devices", 8)
            foundData(res).send(jsonStr)
          else
            notFoundData(res).end()
        )
  )

  createStateRoute("pzemmeasures.html")
  createStateRoute("pzemmeasures.js")

  private def mkSaveMeasure(measurer:String, v:Float, c:Float, p:Float, e:Float, f: Float, pf:Float, res: HttpServerResponse):Unit =
    if v == 0.0 || c == 0.0 || p == 0.0 || e == 0.0 || measurer.isEmpty then
      notFoundData(res)
      res.end()
    else
      val time = Date().getTime
      val query = s"insert into pzem_info values ($time, '$measurer', $v, $c, $p, $e, $f, $pf)"
      val client = dbCon.client
      client.query(query).execute()
        .onComplete(ar =>
          if ar.succeeded() then foundData(res).end()
          else notFoundData(res).end()
        ).onFailure(e => MainLogger.infoLog(s"Cannot add pzem_info $measurer"))

