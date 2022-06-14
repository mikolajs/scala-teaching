package org

import java.io.{File, FileInputStream, FileOutputStream, IOException}
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipFileTest {
  @throws[IOException]
  def test(): Unit = {
    val sourceFile = "/home/ms/Pobrane/test1.txt"
    val ss = scala.io.Source.fromFile(sourceFile, "ISO8859-1")
    val array = ss.map(_.toByte).toArray
    val fos = new FileOutputStream("/home/ms/Pobrane/compressed.zip")
    val zipOut = new ZipOutputStream(fos, StandardCharsets.ISO_8859_1)
    val fileToZip = new File(sourceFile)
    //val fis = new FileInputStream(fileToZip)
    val zipEntry = new ZipEntry(fileToZip.getName)
    zipOut.putNextEntry(zipEntry)
    zipOut.write(array)
//    val bytes = new Array[Byte](1000)
//    var length = 0
//    do {
//      length = fis.read(bytes)
//      if(length >= 0) zipOut.write(bytes, 0, length)
//    } while(length > 0)
    zipOut.close()
    //fis.close()
    fos.close()
  }
}
