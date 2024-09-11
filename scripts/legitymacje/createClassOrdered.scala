case class Pupil(val PESEL:String, val firstName:String, val lastName:String, val className:String){
  def toCSV = s"$lastName;$firstName;$PESEL"
}

def readCSV =
  val data = scala.io.Source.fromFile("uczniowie.csv").getLines.toList.drop(1).map(_.split(";")).filter(_.size > 4)
  for line <- data yield
    Pupil(line(4), mkLowerRest(line(2)), mkLowerRest(line(3)), line(0))
  
def mkLowerRest(name:String) =
  val all = name.split(" ").map(_.trim).filter(_.size > 0)
  all.map(n => n.take(1) + n.drop(1).toLowerCase).mkString(" ")

@main def main(className:String) = 
  val str = readCSV.filter(p => p.className == className).sortBy(_.lastName).map(_.toCSV).mkString("\n")
  val path = java.nio.file.Paths.get("klasa.csv")
  java.nio.file.Files.write(path, str.getBytes)
