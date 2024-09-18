import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._

case class Boiler(t:Long, T:Float)
case class CheckBoiler(ch:String, tT:Long, TT:Float, tB:Long, TB:Float)

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
    sql"select time, temperature from boiler order by time asc limit $size".query[Boiler].to[List].transact(xa).unsafeRunSync()

  def checkData(last:Int):List[CheckBoiler] =
    sql"""select measure.thermometer, measure.time, measure.temperature, boiler.time, boiler.temperature
        | from measure, measure_used, boiler
        | where measure_used.thermometer = measure.thermometer and
        | measure.time = measure_used.measure_time
        | and boiler.time = measure_used.boiler_time
        | order by boiler.time desc limit $last""".stripMargin.query[CheckBoiler].to[List].transact(xa).unsafeRunSync()