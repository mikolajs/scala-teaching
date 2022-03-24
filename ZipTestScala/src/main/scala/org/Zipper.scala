package org

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream}
import java.nio.charset.StandardCharsets
import java.util.zip.{CRC32, Deflater, ZipEntry, ZipInputStream, ZipOutputStream}
import com.sun.xml.messaging.saaj.util.ByteOutputStream

import java.nio.file.Files

class Zipper(compressor: DeflateAndInflate) {

  def toZipOneFile(text: String, file:String): Array[Byte] = {
    val out = new ByteOutputStream()
    val zip = new ZipOutputStream(out)
    zip.setMethod(ZipOutputStream.DEFLATED)
    val bytes = text.getBytes(StandardCharsets.UTF_8)
    //val zipped = compressor.mkCompress(bytes)
    val entry = new ZipEntry(file)
    entry.setSize(bytes.length)
    entry.setCompressedSize(bytes.length)
    zip.putNextEntry(entry)
    zip.write(bytes, 0, bytes.length)
    zip.closeEntry()
    val zipData = out.getBytes
    zip.flush()
    zip.finish()
    zip.close()
    zipData
  }

  def toZipFilesFromBytes(map: Map[String, Array[Byte]]): Array[Byte] = {
    val out = new ByteOutputStream()
    val zip = new ZipOutputStream(out)
    zip.setMethod(ZipOutputStream.DEFLATED)
    zip.setLevel(Deflater.NO_COMPRESSION)
    map.keys.map(key => {
      val bytes = map(key)
      val entry = new ZipEntry(key)
      zip.putNextEntry(entry)
      zip.write(bytes)
      zip.flush()
     // zip.write(bytes, 0, bytes.length)
      zip.closeEntry()
    })
    val zipData = out.getBytes
    out.close()
    zip.close()
    zipData
  }
  def toZipOneTextAndFiles(text: String, file: String, binMap: Map[String, Array[Byte]]): Array[Byte] = {
    val out = new ByteOutputStream()
    val zip = new ZipOutputStream(out)
    zip.setMethod(ZipOutputStream.DEFLATED)
    //zip.setLevel(Deflater.NO_COMPRESSION)
    val bytes = text.getBytes(StandardCharsets.UTF_8)
    //val zipped = compressor.mkCompress(bytes)
    val entry = new ZipEntry(file)
    entry.setSize(bytes.length)
    entry.setCompressedSize(bytes.length)
    zip.putNextEntry(entry)
    zip.write(bytes, 0, bytes.length)
    zip.closeEntry()

    addBinFiles(binMap, zip)

    val zipData = out.getBytes
    zip.flush()
    zip.finish()
    zip.close()
    zipData
  }

  def addBinFiles(map: Map[String, Array[Byte]], zip: ZipOutputStream): Unit= {
    map.keys.map(key => {
      val bytes = map(key)
      val entry = new ZipEntry("pliki/" + key)
      //val crc32 = new CRC32()
      //crc32.update(bytes)
      //entry.setCrc(crc32.getValue)
      //entry.setSize(bytes.length)
      zip.putNextEntry(entry)
      zip.write(bytes)
      zip.flush()
      // zip.write(bytes, 0, bytes.length)
      zip.closeEntry()
    })
  }

  def toZipFiles(map: Map[String, String]): Array[Byte] = {
    val out = new ByteOutputStream()
    val zip = new ZipOutputStream(out)
    zip.setMethod(ZipOutputStream.DEFLATED)
    map.keys.map(key => {
      val bytes = map(key).getBytes(StandardCharsets.UTF_8)
      val entry = new ZipEntry(key)
//      entry.setSize(bytes.length)
//      entry.setCompressedSize(bytes.length)
      zip.putNextEntry(entry)
      zip.write(bytes, 0, bytes.length)
      zip.closeEntry()
    })
    val zipData = out.getBytes
    out.close()
    zip.close()
    zipData
  }

  def fromZipTextOnly(array: Array[Byte]): Map[String, String] = {
    val input = new ByteArrayInputStream(array)
    val zip = new ZipInputStream(input)
    var map = Map[String, String]()
    var entry:ZipEntry = null
    val buffer = new Array[Byte](array.length*10)
    do {
      entry = zip.getNextEntry
      if(entry != null) {
        val out = new ByteArrayOutputStream()
        val key = entry.getName
        var len = 0
        do {
          len = zip.read(buffer)
          if(len > 0) {
            out.write(buffer, 0, len)
          }
        }
        while(len > 0)
        val b = compressor.mkDecompress(out.toByteArray)
        map = map + (key -> new String(b, 0, b.length, StandardCharsets.UTF_8))
        out.close()
      }
    } while (entry != null)
    input.close()
    zip.close()
    map
  }

  def fromZip(array: Array[Byte]): Map[String, String] = {
    val input = new ByteArrayInputStream(array)
    val zip = new ZipInputStream(input)
    var map = Map[String, String]()
    var entry:ZipEntry = null
    val buffer = new Array[Byte](array.length*10)
    do {
      entry = zip.getNextEntry
      if(entry != null) {
        val out = new ByteArrayOutputStream()
        val key = entry.getName
        var len = 0
        do {
          len = zip.read(buffer)
          if(len > 0) {
            out.write(buffer, 0, len)
          }
        }
        while(len > 0)
        val b = out.toByteArray
        map = map + (key -> new String(b, 0, b.length, StandardCharsets.UTF_8))
        out.close()
      }
    } while (entry != null)
    input.close()
    zip.close()
    map
  }

  /*
  def createReport() = {
    try {
      val byteArrayOutputStream = new ByteArrayOutputStream()
      val zipOutputStream = new ZipArchiveOutputStream(byteArrayOutputStream)

      zipOutputStream.setMethod(ZipArchiveOutputStream.STORED);
      zipOutputStream.setEncoding(ENCODING);

      String text= "text";
      byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);

      ArchiveEntry zipEntryReportObject = newStoredEntry("file.txt", textBytes);
      zipOutputStream.putArchiveEntry(zipEntryReportObject);
      zipOutputStream.write(textBytes);

      zipOutputStream.closeArchiveEntry();
      zipOutputStream.close();

      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      return null;
    }

    ArchiveEntry newStoredEntry(String name, byte[] data) {
      ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
      zipEntry.setSize(data.length);
      zipEntry.setCompressedSize(zipEntry.getSize());
      CRC32 crc32 = new CRC32();
      crc32.update(data);
      zipEntry.setCrc(crc32.getValue());
      return zipEntry;
    }

   */


  }
