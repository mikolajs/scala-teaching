package eu.brosbit
import io.vertx.core.Vertx
import io.vertx.pgclient.*
import io.vertx.sqlclient.{PoolOptions, SqlClient}

import scala.jdk.CollectionConverters

case class DBConfig(var port:Int = 5432, var host:String = "localhost", var dbNameAndUser:String = "iothome",
                    var password:String ="")
class DBConnect(vertx:Vertx):
  println("DBCONNECT VERTX")
  private val dbConf = loadDBConfig
  private val connectOptions = PgConnectOptions()
    .setPort(dbConf.port).setHost(dbConf.host).setDatabase(dbConf.dbNameAndUser)
    .setUser(dbConf.dbNameAndUser).setPassword(dbConf.password)
  private val poolOptions = PoolOptions().setMaxSize(4)
  println(dbConf)
  val client: SqlClient = PgBuilder.pool().`with`(poolOptions).connectingTo(connectOptions).using(vertx).build()
  println("Connected")
  def printInfo():Unit = {
    println(dbConf)
    println(client.toString)
  }

  def selectWateringDeviceNames():Unit =
    client.query("select distinct device from watering_info").execute()
      .onComplete(ar =>
        if ar.succeeded() then {
          val rows = ar.result()
          rows.forEach(row =>
            println(row.deepToString())
          )

        } else println("error db")
        client.close()
      )

  private def loadDBConfig :DBConfig = {
    val source = scala.io.Source.fromFile("/etc/iothome/database.cfg")
    val dbC = DBConfig()
    source.getLines().toList.map(line => line.split("="))
      .filter(arr => arr.length == 2).foreach( arr =>

        arr(0) match {
          case "port" => dbC.port = try arr(1).toInt catch case e => 5432
          case "host" => dbC.host = try arr(1).trim catch case e => "localhost"
          case "dbname" => dbC.dbNameAndUser = try arr(1).trim catch case e => "iothome"
          case "password" => dbC.password = try arr(1).trim catch case e => "123456789"
          case _ =>
        }
      )
    source.close()
    //println(dbC)
    dbC
  }
    
