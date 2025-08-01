package eu.brosbit

import eu.brosbit.DBConnect
import io.vertx.core.Vertx
//import org.scalameta.*
import eu.brosbit.*

import java.util.Date
import java.util.Calendar




class MyTests extends munit.FunSuite {
  val vertx = Vertx.vertx()
  val dbCon = DBConnect(vertx)
  test("insert db data") {
    println("start test 1")
    insertMockupDB()
    assertEquals("", "")
  }
  //for chart
  def insertMockupDB(): Unit = {
    val now = Date().getTime
    val rand = scala.util.Random()
    for i <- 0 to 30000000 by 180000 do
      val tt = now - i
      val client = dbCon.client
      val time = Date().getTime
      val parTherms = List("AA", "BB", "CC")
      for parTherm <- parTherms do
        val aTime = tt - rand.nextLong(500)
        val parTemp = rand.nextFloat() * 6.0f + 18.0f
        val query = s"insert into measure  (th, time, T) values ('$parTherm', $aTime, $parTemp)"
        client.query(query).execute()
          .onComplete(ar =>
            if !ar.succeeded() then println("Error insert measure")
          )
      val T = if i < 15000000 then 20.0f else 22.0f
      val query = s"insert into expected_temperature values ($tt, $T)"
      client.query(query).execute()
        .onComplete(ar =>
          if !ar.succeeded() then MainLogger.infoLog("Temperature set boiled NOT succeed")
        )
      val bTT = rand.nextFloat() * 6 + 20.0f
      val query2 = s"insert into boiler_set_temperature values ($tt, $bTT)"
      client.query(query).execute()
        .onComplete(ar =>
          if !ar.succeeded() then MainLogger.infoLog("Temperature set boiled NOT succeed")
        ).onFailure(e => MainLogger.infoLog("Error set boiler temp\n" + e.getMessage))

      val aTT = tt - rand.nextLong(1000L)
      val returnTemp = 25.0f + rand.nextFloat()*4
      val boilerTemp = 27.0f + rand.nextFloat()*4
      val setpointBounds = 40.0f
      val   oemDiagnostic =  1234
      val query3 =
        s"""insert into boiler_info (time, return_temperature, boiler_temperature, setpoint_bound, oem_diagnostic) values
           | ($aTT, $returnTemp, $boilerTemp, $setpointBounds, $oemDiagnostic)""".stripMargin
      client.query(query3).execute()
        .onComplete(ar =>
          if !ar.succeeded() then println("Not added boiler info")
        )
  }
  
}