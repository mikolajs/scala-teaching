package eu.brosbit

import com.typesafe.scalalogging.Logger

object MainLogger:
  private val logger = Logger("iotMainLogger")
  def infoLog(info:String):Unit = logger.info(info)
