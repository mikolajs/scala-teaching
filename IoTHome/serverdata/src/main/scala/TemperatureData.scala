import java.util.{Calendar, Date, GregorianCalendar}

case class TemperatureMeasure(t:Long, T:Float)

object TemperatureData:
  private val MAX_SIZE = 20
  private val THERMOMETERS = 2
  private val MAX_TEMP = 40
  private val DELTA_TIME = 240000L
  private val lastTemperatures = scala.collection.mutable.Map[Char, TemperatureMeasure]()
  private val tableTemperaturesWorkingDay = scala.collection.mutable.Map[Int, Float](
    4 -> 20.0f, 6 -> 18.0f, 15 -> 21.0f, 22 -> 18.0f
  )
  private val tableTemperaturesWeekendDay = scala.collection.mutable.Map[Int, Float](
    8 -> 20.0f, 14 -> 19.0f, 16 -> 21.0f, 22 -> 18.0f
  )
  private var T_boiler = 20f

  def getAfterTime:Float =
    val dT = countChangeTemp
    lastTemperatures.clear()
    if T_boiler + dT > MAX_TEMP then
      T_boiler = MAX_TEMP
    else
      T_boiler += dT
    T_boiler

  def add(C:Char, t:Long, T:Float):Float =
    if lastTemperatures.contains(C) then
      val dT = countChangeTemp
      lastTemperatures += (C -> TemperatureMeasure(t, T))
      setTempBoiler(dT)
      scala.math.round(T_boiler*10.0f)/10.0f
    else
      lastTemperatures += (C -> TemperatureMeasure(t, T))
      if lastTemperatures.size == THERMOMETERS then
        setTempBoiler(countChangeTemp)
        lastTemperatures.clear()
        scala.math.round(T_boiler*10.0f)/10.0f
      else -1.0

  private def setTempBoiler(dT: Float): Unit =
    if T_boiler + dT > MAX_TEMP then
      T_boiler = MAX_TEMP
    else
      T_boiler += dT

  private def countAvgTemperature = lastTemperatures.map(_._2.T).sum / lastTemperatures.size

  private def countChangeTemp: Float =
    val T_expected = checkExpectedTemp
    val dT = T_expected - countAvgTemperature
    if dT > 12.0f then 6.0f
    else if dT.abs > 0.2f then dT * 0.5f
    else 0.0f

  private def checkExpectedTemp:Float =
    val gCal = Calendar.getInstance()
    val hh = gCal.get(Calendar.HOUR_OF_DAY)
    val dow = gCal.get(Calendar.DAY_OF_WEEK)
    val tmp = if dow == 7 || dow == 1 then tableTemperaturesWeekendDay else tableTemperaturesWorkingDay
    var T = 0.0f
    for map <- tmp do
      if  map._1 <=  hh then T = map._2
    if T == 0.0f then T = tmp.head._2
    T