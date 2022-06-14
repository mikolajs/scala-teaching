package org

import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

object Main {

  def main(arr:Array[String]): Unit = {
    val file = "/home/ms/Pobrane/test1.txt"
//    val path = "/home/ms/Pobrane/export.zip"
    val compressor = new DeflateAndInflate()
    val text = scala.io.Source.fromFile(file, "UTF-8").getLines().mkString("\n");
//    val bytes = text.map(_.toByte).toArray
//    println("Tekst: " + text)
//    println("Rozmiar: " + text.length)
//    val zipped = compressor.mkCompress(bytes)
//    testInflate(zipped, compressor)

    val zipper = new Zipper(compressor)


//    val bytesZipped = zipper.toZipOneFile(text, "test1.txt")
//    saveZipFile(bytesZipped, path)
//
//    val newZip = readZipFile(path)
//    println("Test Read ZIP: ")
//    println("Red zip form file length: " + newZip.length)
//    println("Odczytuję dane")
//    val dataMap = zipper.fromZip(newZip)
//    dataMap.keys.foreach( key => {
//      val text = dataMap(key)
//      println("Odczytany tekst " + key + " o długości: " + text.length)
//      println("Zawartość: " + text)
//    })
//
    saveImagesTest(text, zipper)
//    val zf = new ZipFileTest()
//    zf.test()
  }

  private def readZipFile(path:String): Array[Byte] ={
    val ss = scala.io.Source.fromFile(path, "ISO8859-1")
    ss.map(_.toByte).toArray
  }
  private def saveZipFile(data:Array[Byte], path:String):Unit ={
    import java.nio.file.Files
    import java.nio.file.Paths
    Files.write(Paths.get(path), data)
    Thread.sleep(1000)
  }

  def testInflate(zipped: Array[Byte], compressor: DeflateAndInflate): Unit ={
    println("Zipped length: " + zipped.length)
    println("Zipped: " + new String(zipped, 0, zipped.length, StandardCharsets.UTF_8))
    val unzipped = compressor.mkDecompress(zipped)
    println("Unipped length: " + unzipped.length)
    println("Unziped: " + new String(unzipped, 0, unzipped.length, StandardCharsets.UTF_8))
  }

  def saveImagesTest(text: String, zipper: Zipper) = {
    val base = "/home/ms/Pobrane/"
    val paths = List(
      "ofsp.jpg", "down_money.png", "cat.jpg", "up_money.png"
    )
    val dataMap:Map[String, Array[Byte]] = paths.map(p => {
      val bytes = readZipFile(base+p)
      (p, bytes)
    }).toMap
    val zipped = zipper.toZipOneTextAndFiles(text, "data.json", dataMap)
    saveZipFile(zipped, "/home/ms/Pobrane/export3.zip")
  }



}
