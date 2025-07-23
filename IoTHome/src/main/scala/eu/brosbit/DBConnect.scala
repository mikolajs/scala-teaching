package eu.brosbit

import doobie.*
import doobie.implicits.*
import doobie.util.ExecutionContexts
import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*

import java.util.Date
///TODO: change to data insert by vertx native
case class Boiler(t:Long, T:Float):
  def toJson = s"""{"t":$t, "T":$T}"""
case class CheckBoiler(ch:String, tT:Long, TT:Float, tB:Long, TB:Float)
case class CheckMeasure(th:String, t:Long, T:Float):
  def toJson = s"""{"th":"$th", "t":$t, "T":$T}"""
  
case class BoilerInfo(t:Long, rt:Float, bt:Float, sb:Float, oem:Int):
  def toJson = s"""{"time":$t, "returnTemp":$rt, "boilerTemp":$bt, "setpointBound":$sb, "oemDiagnostic":$oem}"""
  
case class CameraImageInfo(createTime:Long, cameraName:String)
case class PzemMeasure(createTime:Long, pzemName:String, v:Float, c:Float, p:Float, e:Float, f:Float, pf:Float)
case class WateringInfo(wateringTime:Long, device:String)

object DBConnect:
  import cats.effect.unsafe.implicits. global
  private val xa = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver", // JDBC driver classname
    url = "jdbc:postgresql:iothome", // Connect URL - Driver specific
    //url = "jdbc:postgresql://192.168.0.120/iothome", // Connect URL - Driver specific
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

  def selectBoilerSetTemperature(size: Int = 100): List[Boiler] =
    sql"select time, T from boiler_set_temperature order by time desc limit $size".query[Boiler].to[List].transact(xa).unsafeRunSync()

  def selectLastMeasures(last: Int = 200): List[CheckMeasure] =
    sql"select th, time, T from measure order by time desc limit $last".query[CheckMeasure].stream.compile.toList
      .transact(xa).unsafeRunSync()
    
  def selectLastBoilerExpectedTemperature(last: Int = 100): List[Boiler] =
    sql"select time, T from expected_temperature order by time desc limit $last".query[Boiler].stream.compile.toList
      .transact(xa).unsafeRunSync()
   
  def selectLastBoilerInfo(last: Int = 100): List[BoilerInfo] =
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

  /// FOR CAMERA
  def insertCameraImageInfo(camName:String, createTime:Long):Int =
    sql"""insert into camera_images_info values ($createTime, $camName)"""
      .update.run.transact(xa).unsafeRunSync()
  
  def getCameraImages(dir:String, from:Long, limit:Int = 24):List[CameraImageInfo] = {
    //println(dir + " " + from)
    sql"""select * from camera_images_info where camera_name = $dir and create_time > $from limit $limit"""
      .query[CameraImageInfo].stream.compile.toList.transact(xa).unsafeRunSync()
  }

  def getCameraNames: List[String] =
    sql"""select distinct camera_name from camera_images_info"""
      .query[String].stream.compile.toList.transact(xa).unsafeRunSync()
      
  ///FOR PZEM
  def insertPZEMMeasure(create_time:Long, measurer:String, v:Float, c:Float, p:Float, e:Float, f:Float, pf:Float):Int =
    sql"""insert into pzem_info values ($create_time, $measurer, $v, $c, $p, $e, $f, $pf)"""
      .update.run.transact(xa).unsafeRunSync()
      
  def selectPzemNames():List[String] =
    sql"""select distinct measurer from pzem_info"""
      .query[String].stream.compile.toList.transact(xa).unsafeRunSync()
      
  def selectPzemMeasures(pzemName:String, limit:Int = 120):List[PzemMeasure] =
    sql"""select * from pzem_info where measurer = $pzemName order by create_time DESC limit $limit"""
      .query[PzemMeasure].stream.compile.toList.transact(xa).unsafeRunSync()

  ///Watering flower and tunel
  def insertWatering(watering_time:Long, device: String):Int =
    sql"""insert into watering_info values($watering_time, $device)"""
      .update.run.transact(xa).unsafeRunSync()

  def selectWateringDeviceNames():List[String] =
    sql"""select distinct device from watering_info"""
      .query[String].stream.compile.toList.transact(xa).unsafeRunSync()

  def selectWateringLast(device:String, limit:Int = 30):List[Long] =
    sql"""select watering_time from watering_info where device = $device order by watering_time DESC limit $limit"""
       .query[Long].stream.compile.toList.transact(xa).unsafeRunSync()
