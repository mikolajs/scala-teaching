package org

import java.util.zip.{Deflater, Inflater}

class DeflateAndInflate {

  def mkDecompress(compressed: Array[Byte]):Array[Byte]= {
    val decompressor: Inflater = new Inflater
    decompressor.setInput(compressed, 0, compressed.length)
    val result: Array[Byte] = new Array[Byte](compressed.length*10)
    val resultLength: Int = decompressor.inflate(result)
    decompressor.end()
    result.take(resultLength)
  }

  def mkCompress(plain: Array[Byte]): Array[Byte] ={
    val compress = new Deflater()
    compress.setInput(plain)
    val zipped = new Array[Byte](plain.length)
    compress.finish()
    val size = compress.deflate(zipped)
    zipped.take(size)
  }

}
