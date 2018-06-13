package eu.brosbit

import java.time.LocalDate
import scala.io.Source
import scala.io.Source
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import java.time.LocalDate

case class Delay(delayInSeconds:Int, estimatedTime:String, headsign:String, vehicleId: Int, routeId: Int){
  override def toString = s"Linia: $routeId, Kierunek: $headsign, Czas odjazdu: $estimatedTime"  
}
case class Arrival(lastUpdate: String, delay: List[Delay]){
  override def toString = delay.mkString("\n")
}

class ArriveTime {

  def get(id:Int) = getVehicles(id).toString
  
  
  //pojazdy na przystanku
  val urlVehicles = "http://87.98.237.99:88/delays?stopId="
  
   def getVehicles(id:Int) = {
    val pvo = parseVehicles(id).toOption
    if(!pvo.isEmpty) pvo.getOrElse(Arrival("", Nil))
    else Arrival("", Nil)
    }

  private def loadVehicles(id:Int)  = {
    val i = Source.fromURL(urlVehicles + id.toString).getLines
    var s = ""
    while(i.hasNext) s += i.next
    s
  }
  
  private def parseVehicles(id:Int) =  decode[Arrival](loadVehicles(id))

 
}