package eu.brosbit

import io.vertx.ext.web.Router
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod

class VariousActionsRouter(vertx:Vertx, router:Router) extends RouterBase (vertx, router):
  router.route(HttpMethod.GET, "/getChristmasTreeState").handler(ctx =>
    val state = VariousActionData.getChristmasTreeInfo
    val json = s"""{"state":$state}"""
    val res = ctx.response()
    res.setStatusCode(200).putHeader("Content-Type", "application/json").send(json)
  )
  router.route(HttpMethod.GET, "/setChristmasTreeState").handler(ctx =>
    val s = getParamInt("state", ctx)
    val r = VariousActionData.setChristmasTreeState(s)
    val res = ctx.response()
    res.putHeader("Content-Type", "plain/text")
    if r then
      res.setStatusCode(200).send("OK")
    else
      res.setStatusCode(404).send("Error")
  )
  createStateRoute("christmastree.html")
  createPictureRoute("chtree.png")
