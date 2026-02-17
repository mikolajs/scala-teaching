///https://www.jopendocument.org/docs/index.html
import org.jopendocument.dom.text.TextDocument
import org.jopendocument.dom.text.Paragraph
import java.io.File
import java.util.Scanner

def openTxtFile(name:String):Array[String] =
  var txt = ""
  val txtFile = File(name)
  if txtFile.exists() then
    val scanner = Scanner(txtFile)
    while scanner.hasNextLine() do
      txt += scanner.nextLine() + "\n"
  else 
    println("ERROR FILE NOT EXISTS!")
    txt = "Błąd brak pliku"
  txt.split("\n")


@main def hello(): Unit =
  val outFile = File("test.odt")
  if !outFile.exists() then
    outFile.createNewFile()
  val p1 = Paragraph("To jest pierwszy akapit z DOM Paragraph")
  val doc = TextDocument.createEmpty("")
  doc.add(p1)
  val data = openTxtFile("pliktestu.txt")
  for txt <- data do
    doc.add(Paragraph(txt))
  doc.saveAs(outFile)
  org.jopendocument.dom.OOUtils.open(outFile)
  //val engineTemp = RhinoTemplate(outFile)
  //engineTemp.setField("Title", "To jest tytuł")
  //engineTemp.saveAs(outFile)
  


