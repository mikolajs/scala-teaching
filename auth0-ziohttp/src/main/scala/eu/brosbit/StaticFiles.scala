package eu.brosbit

import java.io.File

object StaticFiles:
  val start = "src/main/resources"
  def getIndex = new File(s"$start/index.html")

  def getCss(name:String) = new File(s"$start/css/$name")

  def getHtml(name:String) = new File(s"$start/$name")

  def getJavaScript(name:String) = new File(s"$start/js/$name")

  def getImg(name:String) = new File(s"$start/img/$name")