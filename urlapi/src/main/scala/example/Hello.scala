package example

import com.softwaremill.sttp.HttpURLConnectionBackend
import com.softwaremill.sttp.quick._
import ujson.Js

object Hello extends Greeting with App {
  println(greeting)
  implicit val backend = HttpURLConnectionBackend()
  val res = sttp.get(uri"http://api.openweathermap.org/data/2.5/weather?q=Gdansk,pl&APPID=b1ec73e9fefb5cb76fb4d05f06b75d3a&lang=pl")
      .send()
  val data = res.body.getOrElse("EMPTY")

  val json = ujson.read(data)
  println(s"Miasto: ${json("name").str}")
  printf("Temperatura: %.2f stopni Celcjusza\n", json("main").obj("temp").num - 273.15)
  printf("Niebo: %s", json("weather")(0).obj("description").str)



}

trait Greeting {
  lazy val greeting: String = "hello"
}
