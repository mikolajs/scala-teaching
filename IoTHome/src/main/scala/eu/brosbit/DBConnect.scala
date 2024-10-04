package eu.brosbit

import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._

case class Boiler(t:Long, T:Float):
  def toJson = s"""{"t":$t, "T":$T}"""
case class CheckBoiler(ch:String, tT:Long, TT:Float, tB:Long, TB:Float)
case class CheckMeasure(th:String, t:Long, T:Float):
  def toJson = s"""{"th":"$th", "t":$t, "T":$T}"""
  
case class BoilerInfo(t:Long, rt:Float, bt:Float, sb:Float, oem:Int):
  def toJson = s"""{"time":$t, "returnTemp":$rt, "boilerTemp":$bt, "setpointBound":$sb, "oemDiagnostic":$oem}"""
  

object DBConnect:
  import cats.effect.unsafe.implicits. global
  private val xa = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver", // JDBC driver classname
    url = "jdbc:postgresql:iothome", // Connect URL - Driver specific
    user = "iothome", // Database user name
    password = "zaq12wsx", // Database password
    logHandler = None // Don't setup logging for now. See Logging page for how to log events in detail
  )
  val y = xa.yolo
  import y._

  private def insertMeasure(therm:Char, t:Long, T:Float) =
    //val thermometer = Fragments.
    sql"""insert into measure  (thermometer, time, temperature) values (${therm.toString}, $t, $T)""".update

  private def insertBoiler(t:Long, T:Float) =
    sql"insert into boiler (time, temperature) values ($t, $T)".update

  private def insertMeasureUsed(therm:Char, measure_time: Long, boiler_time: Long) =
    sql"""insert into measure_used (thermometer, measure_time, boiler_time)
         values (${therm.toString}, $measure_time, $boiler_time)""".update

  def mkInsertMeasure(therm:Char, t:Long, T:Float):Int =
    insertMeasure(therm, t, T).run.transact(xa).unsafeRunSync()

  def mkInsertBoiler(t:Long, T:Float):Int =
    insertBoiler(t, T).run.transact(xa).unsafeRunSync()

  def mkInsertMeasureUsed(therm:Char, tT: Long, tB: Long): Int =
    insertMeasureUsed(therm, tT, tB).run.transact(xa).unsafeRunSync()

  def mkSelectBoilerSet(size: Int): List[Boiler] =
    sql"select time, temperature from boiler order by time desc limit $size".query[Boiler].to[List].transact(xa).unsafeRunSync()

  /*
  def checkData(last:Int):List[CheckBoiler] =
    sql"""select measure.thermometer, measure.time, measure.temperature, boiler.time, boiler.temperature
         | from measure, measure_used, boiler
         | where measure_used.thermometer = measure.thermometer and
         | measure.time = measure_used.measure_time
         | and boiler.time = measure_used.boiler_time
         | order by boiler.time desc limit $last""".stripMargin.query[CheckBoiler].to[List].transact(xa).unsafeRunSync()
  */
  def checkLastMeasures(last: Int = 50): List[CheckMeasure] =
    sql"select thermometer, time, temperature from measure order by time desc limit $last".query[CheckMeasure].stream.compile.toList
      .transact(xa).unsafeRunSync()
    
  def checkLastBoilerSet(last: Int = 25): List[Boiler] =
    sql"select time, temperature from boiler order by time desc limit $last".query[Boiler].stream.compile.toList
      .transact(xa).unsafeRunSync()
    
  def checkBoilerInfo(last: Int = 20): List[BoilerInfo] =
    sql"""select time, return_temperature, boiler_temperature, setpoint_bound, oem_diagnostic from boiler_info 
         | order by time desc limit $last
       """.stripMargin.query[BoilerInfo].stream.compile.toList.transact(xa).unsafeRunSync()
    
  def mkInsertBoilerInfo(t:Long, returnTemp:Float, boilerTemp:Float, setpointBound:Float, oemDiagnostic:Int):Int =
    sql"""insert into boiler_info (time, return_temperature, boiler_temperature, setpoint_bound, oem_diagnostic) values
         | ($t, $returnTemp, $boilerTemp, $setpointBound, $oemDiagnostic)
       """.stripMargin.update.run.transact(xa).unsafeRunSync()