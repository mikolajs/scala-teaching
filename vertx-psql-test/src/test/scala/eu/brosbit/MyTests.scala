package eu.brosbit

import eu.brosbit.DBConnect
//import org.scalameta.*
import eu.brosbit.*

import java.util.Date
import java.util.Calendar




class MyTests extends munit.FunSuite {
  test("insert db data") {
    println("start test 1")
    insertMockupDB()
    assertEquals("", "")
  }
  //for chart
  def insertMockupDB(): Unit = {
    val now = Date().getTime
    val rand = scala.util.Random()
    for i <- 0 to 30000000 by 300000 do
      val tt = now - i
      DBConnect.insertMeasure('A', tt - rand.nextLong(500), rand.nextFloat() * 6.0f + 18.0f)
      DBConnect.insertMeasure('B', tt - rand.nextLong(500), rand.nextFloat() * 6.0f + 18.0f)
      DBConnect.insertMeasure('C', tt - rand.nextLong(500), rand.nextFloat() * 6.0f + 18.0f)
      if i < 15000000 then DBConnect.insertExpectedTemperature(tt, 20.0f)
      else DBConnect.insertExpectedTemperature(tt, 22.0f)
      DBConnect.insertBoilerSetTemperature(tt, rand.nextFloat() * 6 + 20.0f)
      DBConnect.insertBoilerInfo(tt - rand.nextLong(1000L), 25.0f + rand.nextFloat()*4, 27.0f + rand.nextFloat()*4, 40.0f, 1234)
  }
  
}