import scala.io.Source
import java.nio.file.{Paths, Files}

@main def main() =
  val lines = Source.fromFile("text.txt").getLines.toArray
    .map(line => line.trim).filter(line => !line.isEmpty)
  for i <- 0 until lines.size do
    val ch = lines(i).head
    if ch == '.' || ch == ',' || ch == ':' || ch == '?' || ch == '!' then
      lines(i-1) += lines(i)
      lines(i) = ""
  val clearedLines = lines.filter(line => !line.isEmpty)
  val p = Paths.get("text_fixed.txt")
  Files.write(p, clearedLines.mkString("\n").getBytes)


