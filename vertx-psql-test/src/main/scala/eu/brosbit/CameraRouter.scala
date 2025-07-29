package eu.brosbit

import scala.jdk.CollectionConverters.*
import io.vertx.core.Vertx
import io.vertx.core.http.{HttpMethod, HttpServerResponse}
import io.vertx.ext.web.Router
import java.io.File
import java.util.Date
import io.vertx.core.file.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*
import com.github.plokhotnyuk.jsoniter_scala.core.*

case class CameraImg(t: Long, cameraName: String)
given userCodec: JsonValueCodec[List[CameraImg]] = JsonCodecMaker.make

object ImageFileNameCreator:
  def getName(time: Long): String = "vid_" + time.toString + ".jpg"
  val basePath = "/home/ms/cameras/"
  def createFullPath(camName: String, time: Long): String = {
    val name = getName(time)
    s"$basePath/$camName/$name"
  }

class CameraRouter(vertx: Vertx, router: Router, dbCon: DBConnect) extends RouterBase(vertx, router):

  router.route(HttpMethod.POST, "/setImage").handler(ctx =>
    println(ctx.request().absoluteURI())
    val camName = ctx.queryParam("name").asScala.toList match {
      case Nil => ""
      case h :: rest => h.trim
    }
    createDirOfCam(camName)
    val pipe = ctx.request().pipe()
    val fs = vertx.fileSystem()
    println(camName)
    ctx.request().headers().asScala.foreach(h => println(h.getKey + " : " + h.getValue))
    val time = Date().getTime
    fs.open(ImageFileNameCreator.createFullPath(camName, time), OpenOptions())
      .onComplete(ar =>
        if ar.succeeded() then
          println("open file successes")
          val asFile = ar.result()
          pipe.to(asFile).onComplete(hand =>
            if hand.succeeded() then {
              //ctx.response().setStatusCode(200).send("OK")
              saveFileInfo(camName, time, ctx.response())
              //println("stream is successes")
            } else {
              ctx.response().setStatusCode(500).end()
              deleteRestOfFile(camName, time)
              MainLogger.infoLog(s"Camera stream wrong for $camName")
              println("stream is wrong")
            }
          )
        else {
          println("Error write image")
          pipe.close()
          ctx.response().setStatusCode(500).end()
        }
      ).onFailure(err =>
        println("Error: " + err.getMessage)
        ctx.response().setStatusCode(500).end()
      )
  )

  router.route(HttpMethod.GET, "/getCameraImages").handler(ctx =>
    val time: Long = try ctx.queryParam("t").asScala.head.trim.toLong
    catch
      case _ => 0L
    val dir: String = try ctx.queryParam("d").asScala.head.trim.toString
    catch
      case _ => ""
    val res = ctx.response()
    //println(s"parameters $parDir, $parTime")
    if dir.nonEmpty && time > 0L then
      val client = dbCon.client
      val query = s"select create_time, camera_name from camera_images_info where camera_name = '$dir' and create_time > $time order by create_time limit 36"
      client.query(query).execute()
        .onComplete(ar =>
          if ar.succeeded() then {
            val rows = ar.result()
            val camList:List[CameraImg] = DataCreator.createImagesCameraList[CameraImg](rows)
            val data:String = cameraImgListToJson(camList)
            //val data:String = DataCreator.createJsonArray(rows, "devices", 2)
            foundData(res)
            res.send(data)
          }
          else {
            notFoundResource(res)
            res.end()
          }
        ).onFailure(e =>
          MainLogger.infoLog("not found $dir images $time")
        )
    else {
      notFoundResource(res)
      res.end()
    }
  )

  router.route(HttpMethod.GET, "/getCameraNames").handler(ctx =>
    val res = ctx.response()
    val query = "select distinct camera_name from camera_images_info"
    val client = dbCon.client
    client.query(query).execute()
      .onComplete(ar =>
        if ar.succeeded() then
          val rows = ar.result()
          val arr = DataCreator.createStringArray(rows).map(n => s"\"$n\"").mkString(",")
          //println(arr)
          val data = s"""{"devices":[$arr]}"""
          foundData(res)
          res.send(data)
        else {
          notFoundData(res)
          res.end()
        }
      ).onFailure(e =>
        MainLogger.infoLog("Not found cameras\n " + e.getMessage)
      )
  )

  router.route(HttpMethod.GET, "/cameras/*").handler(ctx =>
    //println(ctx.request().uri())
    val fs = vertx.fileSystem()
    val res = ctx.response()
    val uriArr = ctx.request().uri().split("/")
    //println(s"Size of uri ${uriArr.length}")
    if uriArr.length != 4 then
      res.setStatusCode(400).end()
    else {
      val path = ImageFileNameCreator.basePath + uriArr(2) + "/" + uriArr(3)
      fs.readFile(path).onComplete(ar =>
        if ar.succeeded() then
          res.setStatusCode(200).send(ar.result())
        else
          res.setStatusCode(404).end()
      ).onFailure(e =>
        res.setStatusCode(404).end()
      )
    }
  )

  private def saveFileInfo(camName: String, createTime: Long, res:HttpServerResponse): Unit =
    val client = dbCon.client
    val query = s"insert into camera_images_info values ($createTime, '$camName')"
    client.query(query).execute()
      .onComplete(ar =>
        if ar.succeeded() then {
          foundData(res)
          res.end()
        }
      ).onFailure( e =>
        MainLogger.infoLog(s"Cannot insert camera images from $camName")
        MainLogger.infoLog(e.getMessage)
        notFoundResource(res)
        res.end()
      )

  private def deleteRestOfFile(camera:String, time:Long):Unit =
    if camera.trim.nonEmpty then
      val f = File(ImageFileNameCreator.createFullPath(camera, time))
      if f.exists() then
        java.nio.file.Files.delete(java.nio.file.Paths.get(f.toURI))

  private def cameraImgListToJson(list:List[CameraImg]):String =
    val arr = list.map(ci =>
      val path = s"""/cameras/${ci.cameraName}/${ImageFileNameCreator.getName(ci.t)} """
      ci.copy(cameraName = path))
    writeToString(arr)

    //DBConnect.insertCameraImageInfo(camName, createTime) > 0
/*
  def getCameraImages(camName: String, t: Long): String = {
    val cii = DBConnect.getCameraImages(camName, t)
    val content = cii.map(ci =>

    ).mkString(",")
    s"""{"images":[$content]} """
  }

  def getNames: String =
    val names: List[String] = DBConnect.getCameraNames
    val content = names.map(n =>
      s""" "$n" """
    ).mkString(",")
    s"""{"cameraNames":[$content]}"""
  */
  private def createDirOfCam(name:String):Unit =
    if name.trim.nonEmpty then
      val dir = File(ImageFileNameCreator.basePath+name)
      if !dir.exists() then
        java.nio.file.Files.createDirectory(dir.toPath)


  createStateRoute("camera.html")
  createStateRoute("camera.js")
  createStateRoute("picker.min.js")
  createStateRoute("picker.min.css")

