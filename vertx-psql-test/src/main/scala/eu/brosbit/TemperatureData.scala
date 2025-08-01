package eu.brosbit

import java.util.{Calendar, Date}
case class TemperatureMeasure(t: Long, T: Float)

class TemperatureData():
  private val MAX_SIZE = 20
  private val THERMOMETERS = 2
  private val MAX_TEMP = 40.0f
  private val MIN_TEMP = 30.0f
  private val DELTA_TIME = 240000L
  private val lastTemperatures = scala.collection.mutable.Map[String, TemperatureMeasure]()
  private var tableTemperaturesWorkingDay = loadExpectedTempScheduler("workingdays")
  private var tableTemperaturesWeekendDay = loadExpectedTempScheduler("freedays")
  private var T_boiler = 30.0f
  private var TEMP_SETTING = false
  private var TEMP_SETTING_VAL = 21.0f //wykorzystać do ustawienia tempeartury i działanie

  import scala.compiletime.ops.double
  import scala.math.*

  def reloadDataFromFile(): Unit =
    tableTemperaturesWorkingDay = loadExpectedTempScheduler("workingdays")
    tableTemperaturesWeekendDay = loadExpectedTempScheduler("freedays")

  def getOwnSettings = s"""{"settings":{"T":$TEMP_SETTING_VAL, "on":$TEMP_SETTING}}"""

  def setOwnSettings(T: Float, on: Boolean): Unit =
    TEMP_SETTING = on
    TEMP_SETTING_VAL = T

  def getTemperatureForBoiler: Float =
    //gdy jest ustawiona temperatura przez uzytkownika na stronie w przeciwnym wypadku z pliku wg. czasu
    val T_expected_air = if TEMP_SETTING then TEMP_SETTING_VAL else checkExpectedTemp //oczekiwana
    val T_avg_air_now = countAvgTemperature // z pomiarów
    changeBoilerTemp(T_expected_air, T_avg_air_now)
    T_boiler
    
  def getExpectedTemperatureOfAir: Float =
    if TEMP_SETTING then TEMP_SETTING_VAL else checkExpectedTemp
  
  def addMeasure(thName: String, t: Long, T: Float): Unit =
    lastTemperatures += (thName -> TemperatureMeasure(t, T))
    for m <- lastTemperatures do
      if m._2.t < t - 2L * 3600000L then lastTemperatures.remove(m._1)
  
  private def loadExpectedTempScheduler(file: String): Map[Int, Float] =
    val source = scala.io.Source.fromFile(s"/etc/iothome/$file.cfg")
    val map = source.getLines.toList.map(line =>
        if line.trim.head == '#' then List("", "")
        else line.trim.split(" ").filter(_.trim.nonEmpty).toList.take(2)
      ).filter(l => l.length == 2).filterNot(l => l.head.isEmpty)
      .map(l =>
        (l.head.split(":").map(_.toInt).reduce((h, m) => h * 60 + m),
          l.last.toFloat)).toMap
    source.close()
    println(s"for file $file reloaded ${map.size}")
    map

  private def countAvgTemperature = lastTemperatures.map(_._2.T).sum / lastTemperatures.size

  //wylicza o ile zwiększyć temperaturę boilera na podstawie różnić w pomiarach do oczekiwanej powietrza.
  private def countChange(T_expected_air: Float, T_avg_air_now: Float): Float =
    val dT = T_expected_air - T_avg_air_now
    val dTU = if abs(dT) < 0.2 then 0.0f
    else floor(abs(dT)).toFloat + 1.0f
    if dT > 0 then dTU else -dTU

  private def changeBoilerTemp(T_expected_air:Float, T_avg_air_now:Float): Unit =
    //println(s"T_boiler = $T_boiler")
    val dT = countChange(T_expected_air, T_expected_air)
    //println(s"TO $TO, TP = $TP, dt = $dT")
    if T_boiler + dT > MAX_TEMP then T_boiler = MAX_TEMP
    else if T_boiler + dT < MIN_TEMP then T_boiler = MIN_TEMP
    else T_boiler += dT

  private def checkExpectedTemp: Float =
    val gCal = Calendar.getInstance()
    val hh = gCal.get(Calendar.HOUR_OF_DAY)
    val dow = gCal.get(Calendar.DAY_OF_WEEK)
    val tmp = if dow == 7 || dow == 1 then tableTemperaturesWeekendDay else tableTemperaturesWorkingDay
    var T = 0.0f
    for map <- tmp do
      if map._1 <= hh then T = map._2
    if T == 0.0f then T = tmp.head._2
    T