import scala.util.Random
import scala.io.Source
import java.time.LocalDate
import java.nio.file.{Files, Paths}

val r = Random()
  
def createTwoBoards = 
  var str = ""
  val N = r.nextInt(17)+10
  str += N
  str += "\n"
  val ships = drawShips(N*N/5)

  
@main def main():Unit =
  val path = Paths.get("okrety.txt")
  for i <- 1 to 10 do
    str += creatTwoBoards
  Files.write(path, "".getBytes)
   



