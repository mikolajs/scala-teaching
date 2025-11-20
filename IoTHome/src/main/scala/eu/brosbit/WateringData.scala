package eu.brosbit

import java.util.{Calendar, Date, GregorianCalendar}

object WateringData:
  private var wateringMap: Map[String, (Int, Int)] = loadExpectedTempScheduler
  
  def saveWatering(time:Long, device:String):Boolean = {
    MainLogger.infoLog(s"trying to save watering by device: $device at: $time")
    DBConnect.insertWatering(time, device) > 0
  }

  def saveMoisture(time:Long, device:String, moisture:Int):Boolean = {
    MainLogger.infoLog(s"trying to save moisture info by device $device, at: $time val: $moisture")
    DBConnect.insertMoisture(time, device, moisture) > 0
  }

  def getWateringDeviceNames:String =
    val content = DBConnect.selectWateringDeviceNames().map(d =>
      s"\"$d\""
    ).mkString(",")
    s"""{"devices":[$content]}"""

  def getMoistureDeviceNames: String =
    val content = DBConnect.selectMoistureNames().map(d =>
      s"\"$d\""
    ).mkString(",")
    s"""{"devices":[$content]}"""
    
  def getLastWatering(device:String):String =
    val content = DBConnect.selectWateringLast(device).map(t =>
      s"$t"
    ).mkString(",")
    s"""{"wateringTimes":[$content]}"""

  def getLastMoisture(device: String): String =
    val content = DBConnect.selectMoistureLast(device).map(t => t.toJson).mkString(",")
    s"""{"moistureTimes":[$content]}"""
    
  def checkMakeWatering(device:String):Boolean =
    val lastWatering: Long = DBConnect.selectWateringLast(device).headOption.getOrElse(0L)
    val (wateringDistance, wateringHour) = if wateringMap.contains(device) then wateringMap(device) else (-1, -1)
    val gc = GregorianCalendar()
    val now = gc.getTime.getTime
    val distance: Long = (now - lastWatering) / 1000L
    println(s"device $device $lastWatering $wateringHour ${gc.getTime.toString} distace: $distance")
    MainLogger.infoLog(s"device: $device, time not watering: $distance")
    if lastWatering <= 0L && wateringHour < 0 then true
    else if wateringHour < 0 then
      if distance >= wateringDistance*3600L then true
      else false
    else
      val distance: Long = (now - lastWatering) / 1000L
      val hourNow = gc.get(Calendar.HOUR_OF_DAY)
      if hourNow >= wateringHour && distance > wateringDistance*3600L then true
      else false

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
      

