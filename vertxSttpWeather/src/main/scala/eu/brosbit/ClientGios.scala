package eu.brosbit
import io.vertx.core.Vertx
import io.vertx.core.Buffer
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.HttpRequest

class ClientGios():
  val vertx = Vertx.vertx()
  val client = WebClient.create(vertx)
  def getAirCon(id:Int, req:HttpRequest[Buffer]) = 
    client
      .get(80, "api.gios.gov.pl", "/pjp-api/rest/station/sensors/"+id)
      .ssl(true)
      .send()
      .onSuccess(res => 
        if res.statusCode() == 200 then
          println("got air condition")
          req.response()
          .putHeader("content-type", "application/json")
          .putHeader("Access-Control-Allow-Origin", "*")
          .end(res.body())
        else 
          println("wrong answer")
          req.response()
          .putHeader("content-type", "plain/text")
          .putHeader("Access-Control-Allow-Origin", "*")
          .end("Error " + res.statusMessage())
      )
      .onFailure(res =>
        req.response()
          .putHeader("content-type", "application/json")
          .putHeader("Access-Control-Allow-Origin", "*")
          .end("['error':'error']")
     )




