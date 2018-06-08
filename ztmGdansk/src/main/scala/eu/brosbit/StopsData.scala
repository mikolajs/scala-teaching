package eu.brosbit

import java.time.LocalDate
import java.io.IOException
import scala.io.Source
import scala.io.Source
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import java.time.LocalDate

case class Stop(stopId: Int, stopShortName: String, stopDesc: String, stopLat: Double, stopLon: Double)
case class StopsInfo(lastUpdate: String, stops:List[Stop])

class StopsData {
  val urlStops = "http://91.244.248.19/dataset/c24aa637-3619-4dc2-a171-a23eec8f2172/" +
    "resource/cd4c08b5-460e-40db-b920-ab9fc93c1a92/download/stops.json"
  
  val testData = """
    {"2018-06-08": 
      {"lastUpdate":"2018-06-08 12:34",  "stops": 
        [ 
          {"stopId":1212, "stopShortName": "1212", "stopDesc": "Pomorska", "stopLat":23.34, "stopLon": 34.45} 
        ] 
      } 
    }
    """
  
  def getStops = {
    val ps = parseStops.toOption
    if(!ps.isEmpty) ps.getOrElse(Map())(getDateStr)
    else StopsInfo("error", Nil)
    }

  private def loadStops: String = {
    val i = Source.fromURL(urlStops).getLines
    if(i.hasNext) i.next
    else "Error"
  }
  private def getDateStr = LocalDate.now().toString
  
  private def parseStops =  decode[Map[String, StopsInfo]](loadStops)
  
  
  
  
}