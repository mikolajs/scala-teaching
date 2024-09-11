import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.nio.file.{Paths, Files}
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}


val imgReader = ImageIO.getImageReadersBySuffix("PNG").next()

case class Pupil(val PESEL:String, val firstName:String, val lastName:String, val code:String){
  def toFileName = s"$lastName-$firstName-$PESEL-$code"
}

def readCSV =
  val data = scala.io.Source.fromFile("klasa.csv").getLines.toList.map(_.split(";")).filter(_.size > 2)
  for line <- data yield
    Pupil(line(2), line(1), line(0),line(3))

def splitPictures(imageBuf:BufferedImage, w:Int, h:Int, rows:Int, cols:Int, pupils:List[Pupil]) =
  println(s"W: ${imageBuf.getWidth()}, H: ${imageBuf.getHeight}")
  val dx = 82
  val dy = 71
  val W = 348
  val H = 458
  var n = 0
  var x = dx
  var y = dy
  for r <- 0 until rows do
    x = dx
    for c <- 0 until cols do
      println(s"x: $x, y: $y")
      if n < pupils.size then
        val subImageBuf = imageBuf.getSubimage(x+(W-w)/2, y+(H-h)/2, w, h)
        saveFile(pupils(n).toFileName, subImageBuf)
        n += 1
      x += W
    y += H


def saveFile(name:String, imageBuf:BufferedImage) = 
  val outputStream = new ByteArrayOutputStream()
  ImageIO.write(imageBuf, "PNG", outputStream)
  Files.write(Paths.get(s"foto/$name.png"), outputStream.toByteArray())
  



@main def main():Unit =
  val pupils = readCSV
  pupils.foreach(p => println(p.toFileName))
  val path = Paths.get("skan.png")
  val byteArray = Files.readAllBytes(path)
  println(s"Rozmiar pliku: ${byteArray.size}")
  val imageBuf = ImageIO.read(new ByteArrayInputStream(byteArray))
  splitPictures(imageBuf, 330, 430, 6, 5, pupils)
  

  
