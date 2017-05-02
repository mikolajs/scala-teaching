package pl.edu.osp

import java.sql.DriverManager
import java.util.Date

import akka.actor.Actor

class DBActor extends Actor {
  Class.forName("org.h2.Driver")
  val conn = DriverManager.getConnection("jdbc:h2:./test", "sa", "")

  def receive = {
    case LastWeather => sender ! getLatest
    case x:Array[Int] => sender ! insertRow(x)
  }

  override def postStop() {
    conn.close()
  }

  private def getLatest() = {
    val stat = conn.prepareStatement(
      "Select temp, press, humi, wind, sun FROM weather Order By date Desc Limit 1")
    val res = stat.executeQuery()
    val rArr = new Array[Int](5)
    var i = 0
    res.next()
      rArr(0) = res.getInt(1)
      rArr(1) = res.getInt(2)
      rArr(2) = res.getInt(3)
      rArr(3) = res.getInt(4)
      rArr(4) = res.getInt(5)
    rArr
  }

  private def insertRow(arr:Array[Int]): Int = {
    val stat = conn.prepareStatement(
    "Insert into WEATHER (date, temp, press, humi, wind, sun) values (?, ?, ?, ?, ?, ?)")
    stat.setLong(1,(new Date().getTime))
    stat.setInt(2, arr(0))
    stat.setInt(3, arr(1))
    stat.setInt(4, arr(2))
    stat.setInt(5, arr(3))
    stat.setInt(6, arr(4))
    stat.executeUpdate()
  }
}
