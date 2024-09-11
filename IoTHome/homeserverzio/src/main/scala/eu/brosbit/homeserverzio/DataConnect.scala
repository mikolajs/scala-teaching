package eu.brosbit.homeserverzio

import zio.sql.*

case class Temperature(id:String, time:Long, temp:Float)
//given personSchema with zio.schema.DeriveSchema.gen[Temperature]
object DataConnect:
  def getTemperatures:List[(String, Float)] =
    List(("term1", 24.4f), ("term2", 21.4f))