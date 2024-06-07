import scala.io.Source
import java.nio.file.{Paths, Files}
import scala.util.Random

val r = Random()

def save(file:String, data:String) =
  val p = Paths.get(file)
  Files.write(p, data.getBytes)

val imiona = List("IZA", "AGA", "ZOSIA", "ULA", "ADA", "IGOR", "JULIA", "ADAM", "STASZEK", "ZYTA", "ROMAN")

def randomName:String = imiona(r.nextInt(imiona.size))

@main def main() =
  val lines = Source.fromFile("wiadomosci.txt").getLines.toList
    .map(line => line.trim).filter(line => !line.isEmpty).map(line => line.toUpperCase())
  val sh = r.shuffle(lines).map(line => randomName + " " + line)

  save("listy1.txt",sh.take(50).mkString("\n"))
  save("listy2.txt",sh.drop(50).mkString("\n"))

