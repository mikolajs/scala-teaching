package eu.brosbit

import java.io.File
import com.github.tototoshi.csv._

object Maker {

  def mkResults(path:String) = {
    val reader = CSVReader.open(new File(path))
    val results = reader.all()
    println(s"Wszystkich na liście: ${results.size}")
    //results.foreach(r => if (r(3).trim.isEmpty || r(2).trim.isEmpty) println(r))
    val notApeared = results.filter(r => r(2).isEmpty).map(r => Pupil(r(3), r(0), r(1), 0))
    println(s"Not apeared: ${notApeared.size}")
    val pupils = results
      .filter(r => r(3).trim.nonEmpty && r(2).trim.nonEmpty)
      .map(r => Pupil(r(3), r(0), r(1), r(2).toInt))
    for (p <- pupils) if (p.points <= 0) println(p)
    println(s"Wyniki dla posiadających pesel: ${pupils.size}")
    println( s"Ma wyniki ale nie ma peselu: ${results.size - pupils.size - notApeared.size}")
    pupils
  }

  def mkCandidates(path:String) = {
    val candidatesCsv = CSVReader.open(new File(path))
    val candidatesList= candidatesCsv.all()
    val candidates = candidatesList.map(c => {
      Pupil(c(0), c(1), c(2), -1)
    })
    println(s"Ilość kandydatów: ${candidates.size}")
    candidates
  }

  def macher(results:List[Pupil], candidates: List[Pupil]): List[Pupil] = {
    candidates.map(c => {
        var resOpt = results.find(p => p.pesel == c.pesel)
        resOpt match {
            case None => 
            case Some(p) => c.points = p.points  
        }
        c
    })
  }

  def calculatePassedButNotInCandidates(results:List[Pupil], candidates: List[Pupil]) = {
      results.filter(p => candidates.find(c => c.pesel == p.pesel).isEmpty)
  }

  def mkResultsFromSukFile(path:String) = {
    val reader = CSVReader.open(new File(path))
    val results = reader.all().filter(_.length >= 4)
    println(s"Wszystkich na liście: ${results.size}")
    //results.foreach(println)
    //results.foreach(r => if (r(3).trim.isEmpty || r(2).trim.isEmpty) println(r))

    val notAppeared = results.filter(r => r(3).isEmpty).map(r => Pupil(r(0), r(1), r(2), 0, ""))
    println(s"No points: ${notAppeared.size}")
    val pupils = results.filter(r => r(3).trim.nonEmpty)
      .map(r => Pupil(r(0), r(1), r(2), r(3).toInt, ""))

    //for (p <- pupils)  println(p)
    println(s"Wyniki dla posiadających pesel: ${pupils.size}")
    println( s"Ma wyniki ale nie ma peselu: ${results.size - pupils.size - notAppeared.size}")
    pupils++notAppeared
  }

  def mkAccepted(path:String) = {
    val reader = CSVReader.open(new File(path))
    val results = reader.all().filter(_.length >= 4)

    //results.foreach(println)
    //results.foreach(r => if (r(3).trim.isEmpty || r(2).trim.isEmpty) println(r))
    val classInfo = results.map(r => Pupil(r(1), r(2), r(3), 0, r(110).trim))
    println("Wszyscy przyjęci " + classInfo.length)
    //classInfo.foreach(p => p.classInfo.foreach(l => {print(l); print(" ")}))
    classInfo.map(p => {
      if(p.classInfo(2) == ' '  || p.classInfo(2) == '-') p.classInfo = p.classInfo.substring(0,2)
      else p.classInfo = p.classInfo.substring(9, 11)
      p
    })
  }

  def toFile(path: String, pupils:List[Pupil]) {
    val w = CSVWriter.open(new File(path))
    w.writeAll(pupils.map(_.toList))
    w.close()
  }

}
