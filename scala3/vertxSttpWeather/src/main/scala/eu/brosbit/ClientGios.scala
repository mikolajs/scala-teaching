package eu.brosbit
import io.vertx.core.*
import io.vertx.core.buffer._
import io.vertx.ext.web.client.WebClient
import io.vertx.core.http.HttpServerRequest

class ClientGios():
  val vertx = Vertx.vertx()
  val client = WebClient.create(vertx)
  def getAirCon(id:Int, req:HttpServerRequest) = 
    client
      .get(443, "api.gios.gov.pl", "/pjp-api/rest/station/sensors/"+id)
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
          .end("{'Error': '" + res.statusMessage()+"'}")
      )
      .onFailure(res =>
        println("failure!")
        println(res.toString())
        req.response()
          .putHeader("content-type", "application/json")
          .putHeader("Access-Control-Allow-Origin", "*")
          .end("{'error':'error'}")
     )




