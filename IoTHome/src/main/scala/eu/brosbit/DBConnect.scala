package eu.brosbit

import doobie.*
import doobie.implicits.*
import doobie.util.ExecutionContexts
import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*

import java.util.Date

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

  def insertMeasure(therm:Char, t:Long, T:Float):Int =
    sql"""insert into measure  (th, time, T) values (${therm.toString}, $t, $T)""".update
      .run.transact(xa).unsafeRunSync()

  def insertBoilerSetTemperature(t:Long, T:Float):Int =
    sql"insert into boiler_set_temperature (time, T) values ($t, $T)".update
      .run.transact(xa).unsafeRunSync()

 // def mkInsertMeasureUsed(therm:Char, tT: Long, tB: Long): Int =
 //   insertMeasureUsed(therm, tT, tB).run.transact(xa).unsafeRunSync()
  def insertBoilerInfo(t: Long, returnTemp: Float, boilerTemp: Float, setpointBound: Float, oemDiagnostic: Int): Int =
     sql"""insert into boiler_info (time, return_temperature, boiler_temperature, setpoint_bound, oem_diagnostic) values
        | ($t, $returnTemp, $boilerTemp, $setpointBound, $oemDiagnostic)
         """.stripMargin.update.run.transact(xa).unsafeRunSync()

  def insertExpectedTemperature(t:Long, T:Float):Int =
      sql"insert into expected_temperature (time, T) values ($t, $T)".update.run.transact(xa).unsafeRunSync();

  def selectBoilerSetTemperature(size: Int = 60): List[Boiler] =
    sql"select time, T from boiler_set_temperature order by time desc limit $size".query[Boiler].to[List].transact(xa).unsafeRunSync()

  def selectLastMeasures(last: Int = 120): List[CheckMeasure] =
    sql"select th, time, T from measure order by time desc limit $last".query[CheckMeasure].stream.compile.toList
      .transact(xa).unsafeRunSync()
    
  def selectLastBoilerExpectedTemperature(last: Int = 60): List[Boiler] =
    sql"select time, T from expected_temperature order by time desc limit $last".query[Boiler].stream.compile.toList
      .transact(xa).unsafeRunSync()
   
  def selectLastBoilerInfo(last: Int = 60): List[BoilerInfo] =
    sql"""select time, return_temperature, boiler_temperature, setpoint_bound, oem_diagnostic from boiler_info
         | order by time desc limit $last
       """.stripMargin.query[BoilerInfo].stream.compile.toList.transact(xa).unsafeRunSync()

  def mkDeleteOldData():Int =
    val before: Long = new Date().getTime - 10L * 24L * 3600L * 1000L
    mkDeleteOldBoiler(before) +
      mkDeleteOldMeasures(before) +
      mkDeleteOldBoilerInfo(before) +
      mkDeleteOldExpectedTemperature(before)

  private def mkDeleteOldMeasures(before:Long):Int =
    sql"""delete from measures where time < $before""".update.run.transact(xa).unsafeRunSync()

  private def mkDeleteOldBoiler(before:Long):Int =
    sql"""delete from boiler_set_temperature where time < $before""".update.run.transact(xa).unsafeRunSync()

  private def mkDeleteOldBoilerInfo(before:Long):Int =
    sql"""delete from boiler_info where time < $before""".update.run.transact(xa).unsafeRunSync()

  private def mkDeleteOldExpectedTemperature(before: Long): Int =
    sql"""delete from expected_temperature where time < $before""".update.run.transact(xa).unsafeRunSync()



