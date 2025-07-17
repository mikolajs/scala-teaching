package eu.brosbit

import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.core.file.*
import io.vertx.ext.web.handler.BodyHandler

import scala.jdk.CollectionConverters.*
import java.util.Date
import java.io.File
import java.nio.file.attribute.FileAttribute
import java.nio.file.*

object ImageFileNameCreator:
  def getName(time: Long): String = "vid_" + time.toString + ".jpg"
  val basePath = "/home/ms/cameras/"
  def createFullPath(camName:String, time:Long):String = {
    val name = getName(time)
    s"$basePath/$camName/$name"
  }

class CameraRouter(vertx:Vertx, router:Router) extends RouterBase(vertx, router):
  //val router: Router = Router.router(vertx)
  //router.route.handler(BodyHandler.create)

  router.route(HttpMethod.POST, "/setImage").handler( ctx =>
    println(ctx.request().absoluteURI())
    val camName = ctx.queryParam("name").asScala.toList match {
      case Nil => ""
      case h::rest => h.trim
    }
    createDirOfCam(camName)
    val pipe = ctx.request().pipe()
    val fs = vertx.fileSystem()
    println(camName)
    ctx.request().headers().asScala.foreach(h => println(h.getKey + " : " +h.getValue))
    val time = Date().getTime
    fs.open(ImageFileNameCreator.createFullPath(camName, time), OpenOptions())
      .onComplete(ar =>
        if ar.succeeded() then
          println("open file successes")
          val asFile =ar.result()
          pipe.to(asFile).onComplete(hand =>
            if hand.succeeded() then {
              ctx.response().setStatusCode(200).send("OK")
              CameraData.saveFileInfo(camName, time)
              println("stream is successes")
            } else {
              ctx.response().setStatusCode(500).end()
              println("stream is wrong")
            }
          )
        else {
          println("Error write image")
          pipe.close()
          ctx.response().setStatusCode(500).end()
        }
      ).onFailure(err =>
        println("Error: "+err.getMessage)
        ctx.response().setStatusCode(500).end()
      )

  )

  router.route(HttpMethod.GET, "/getCameraImages").handler( ctx =>
    val parTime: Long = try ctx.queryParam("t").asScala.head.trim.toLong
      catch
        case _ => 0L
    val parDir: String = try ctx.queryParam("d").asScala.head.trim.toString
      catch
        case _ => ""
    val res = ctx.response()
    //println(s"parameters $parDir, $parTime")
    if parDir.nonEmpty && parTime > 0L then
      val cameraImageJson = CameraData.getCameraImages(parDir, parTime)
      res.setStatusCode(200)
      res.putHeader("content-type", "application/json").send(cameraImageJson)
    //CameraData.get
    else {
      res.setStatusCode(404)
      res.putHeader("content-type", "plain/text").send("Error")
    }
  )
  
  router.route(HttpMethod.GET, "/getCameraNames").handler(ctx =>
    val res = ctx.response()
    res.setStatusCode(200)
    res.putHeader("content-type", "application/json").send(CameraData.getNames)
  )

  router.route(HttpMethod.GET, "/cameras/*").handler( ctx =>
    //println(ctx.request().uri())
    val fs = vertx.fileSystem()
    val res = ctx.response()
    val uriArr = ctx.request().uri().split("/")
    //println(s"Size of uri ${uriArr.length}")
    if uriArr.length != 4 then
      res.setStatusCode(400).end()
    else {
      val path = ImageFileNameCreator.basePath +  uriArr(2) + "/" + uriArr(3)
      fs.readFile(path).onComplete( ar =>
        if ar.succeeded() then
          res.setStatusCode(200).send(ar.result())
        else
          res.setStatusCode(404).end()
    ).onFailure(e =>
        res.setStatusCode(404).end()
      )
    }
  )

  private def saveImageCamera(name:String, bytes:Array[Byte]): Long =
    if name.trim.nonEmpty then
      val dir = File(ImageFileNameCreator.basePath + name)
      val time = java.util.Date().getTime
      if !dir.exists() then
        java.nio.file.Files.createDirectory(dir.toPath)
      val filePath = ImageFileNameCreator.getName(time)
      Files.write(Paths.get(dir.getAbsolutePath + filePath), bytes)
      time
    else
      0L

  private def createDirOfCam(name:String):Unit =
    if name.trim.nonEmpty then
      val dir = File(ImageFileNameCreator.basePath+name)
      if !dir.exists() then
        java.nio.file.Files.createDirectory(dir.toPath)


  createStateRoute("camera.html")
  createStateRoute("camera.js")
  createStateRoute("picker.min.js")
  createStateRoute("picker.min.css")
