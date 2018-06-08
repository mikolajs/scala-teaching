package eu.brosbit

import scala.util.Try



object Main {
  //
 
  def main(args: Array[String]) {
    val data = new StopsData
    val stops = data.getStops
    val stopName = "Czerwony Dwór"
    var arriveTime = new ArriveTime
    stops.stops.foreach(s => if(s.stopDesc == stopName) {
      println(arriveTime.get(s.stopId))
      Try(Thread.sleep(500))
    })
  }
  
}


