package eu.brosbit

import java.util.{Calendar, Date, GregorianCalendar}

class WateringHelp():
  private var wateringMap: Map[String, (Int, Int)] = loadExpectedTempScheduler

  def devicesNamesToJson(devices:List[String]):String =
    val content = devices.map(d =>
      s"\"$d\""
      ).mkString(",")
    s"""{"devices":[$content]}"""
  

  def reloadScheduler(): Unit = {
    MainLogger.infoLog("reloaded Watering Scheduler")
    println("Reloaded Watering Scheduler")
    wateringMap = loadExpectedTempScheduler
  }

  private def loadExpectedTempScheduler: Map[String, (Int, Int)] =
    val source = scala.io.Source.fromFile(s"/etc/iothome/watering.cfg")
    val map = source.getLines.toList.map(line =>
        if line.trim.head == '#' then List("", "")
        else line.trim.split(" ").filter(_.trim.nonEmpty).toList.take(3)
      ).filter(l => l.length == 3).filterNot(l => l.head.isEmpty)
      .map(l => (l.head, (l(1).toInt, l(2).toInt))).toMap
    source.close()
    //println(s"for file of watering reloaded ${map.size}")
    map
      

