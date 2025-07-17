package eu.brosbit

import java.util.Date

object PZEMData:

  def saveMeasure(measurer:String, v:Float, c:Float, p:Float, e:Float, f: Float, pf:Float):Boolean = {
    if v == 0.0 || c == 0.0 || p == 0.0 || e == 0.0 || measurer.isEmpty then
      false
    else
      DBConnect.insertPZEMMeasure(Date().getTime, measurer, v, c, p, e, f, pf) > 0
  }
  
  def getNames:String = {
    val names =  DBConnect.selectPzemNames().map(n => s""" "$n" """).mkString(",")
    """{"pzem_names":[ """ + names + "]}"
  }
  
  def getMeasures(pzemName:String):String = { 
    val content = DBConnect.selectPzemMeasures(pzemName).map(pzem => 
      s"""{"createTime":${pzem.createTime}, "v":${pzem.v}, "c":${pzem.c}, "p":${pzem.p}, "e":${pzem.e}, "f":${pzem.f}, "pf":${pzem.pf} }"""
    ).mkString(",")
    s"""{"measures":[$content]}"""
  }

