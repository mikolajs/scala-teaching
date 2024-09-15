import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._


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