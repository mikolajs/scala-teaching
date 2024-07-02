import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.nio.file.{Paths, Files}
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}


val imgReader = ImageIO.getImageReadersBySuffix("PNG").next()

case class Pupil(val PESEL:String, val firstName:String, val lastName:String){
  def toFileName = s"$lastName-$firstName-$PESEL"
}

def readCSV =
  val data = scala.io.Source.fromFile("klasa.csv").getLines.toList.map(_.split(";")).drop(1)
  val nrPesel = 0
  val nrFirstName = 1
  val nrLastName = 2
  for line <- data yield
    Pupil(line(nrPesel), line(nrFirstName), line(nrLastName))
  


def splitPictures(imageBuf:BufferedImage, w:Int, h:Int, rows:Int, cols:Int, pupils:List[Pupil]) =
  println(s"W: ${imageBuf.getWidth()}, H: ${imageBuf.getHeight}")
  val dx = 0
  val dy = 0
  var n = 0
  for i <- 0 until cols do
    for j <- 0 until rows do
      val x = j*w + dx
      val y = i*h + dy
      //println(s"x: $x, y: $y")
      if n < pupils.size then
        val subImageBuf = imageBuf.getSubimage(x, y, w, h)
        saveFile(pupils(n).toFileName, subImageBuf)
        n += 1


def saveFile(name:String, imageBuf:BufferedImage) = 
  val outputStream = new ByteArrayOutputStream()
  ImageIO.write(imageBuf, "PNG", outputStream)
  Files.write(Paths.get(s"$name.png"), outputStream.toByteArray())
  



@main def main():Unit =
  val pupils = readCSV
  pupils.foreach(p => println(p.toFileName))
  val path = Paths.get("skan.png")
  val byteArray = Files.readAllBytes(path)
  println(s"Rozmiar pliku: ${byteArray.size}")
  val imageBuf = ImageIO.read(new ByteArrayInputStream(byteArray))
  splitPictures(imageBuf, 410, 511, 6, 6, pupils)
  

  
