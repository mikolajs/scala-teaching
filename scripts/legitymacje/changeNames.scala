
import java.nio.file.{Files, Paths, Path}
import java.io.File
import scala.jdk.CollectionConverters.*

val dir = "foto/"
def getFiles = File(dir).listFiles()
        
def rename(name:String) = 
  val arr = name.split("_").last.split("-")
  s"${arr.head}_${arr.drop(1).mkString("-")}"



@main def main() =
  getFiles.foreach(file =>
      val name = rename(file.getName)
      Files.move(Paths.get(dir+file.getName), Paths.get(dir + name))
  )

   
