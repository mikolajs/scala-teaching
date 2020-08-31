package eu.brosbit

import com.github.tototoshi.csv._
import java.io.File

case class Pupil(
    var pesel: String,
    var fName: String,
    var lName: String,
    var points: Int,
    var classInfo: String = ""
) {

    override def toString() = s"$pesel, $fName, $lName, ${if(points > 0) points.toString else ""}, $classInfo"
    def toList = List(pesel, fName, lName, if(points > 0) points.toString else "")
}

object Main extends App {
//  val results =  Maker.mkResults("SUK_anglisci.csv")
//  var candidates = Maker.mkCandidates("SUK_kandydaci.csv")
//  candidates = Maker.macher(results, candidates)
//  //candidates.foreach(println)
//  val notFound = Maker.calculatePassedButNotInCandidates(results, candidates)
//  println(s"Nie znalezieni na liście kandydatów, a zdający u nas: ${notFound.size}" )
//  //notFound.foreach(println)
//  Maker.toFile("Kandydaci_znalezieni.csv", candidates)
//  Maker.toFile("Kandydaci_nieznalezieni.csv", notFound)
  val suk = Maker.mkResultsFromSukFile("suk_punkty.csv")
  val acc = Maker.mkAccepted("przyjeci.csv")

  acc.foreach(p => {

    val f = suk.find(s => s.pesel == p.pesel)
    if(f.isDefined) p.points = f.get.points
  })
  acc.foreach(println)

}
