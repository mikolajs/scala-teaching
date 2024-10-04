package net.brosbit


import org.junit.Assert._
import org.junit.Test
import eu.brosbit.hexlib._

class Test1 {
  @Test def t1(): Unit = {
    assertEquals("", "")
  }
  @Test def t2():Unit = {
    val checkSize = 100
    val gen = MapGenerator(checkSize)
    val dataMap:Array[Array[Char]] = gen.getMap
    println("Water size: " +  howManyOfType(dataMap, 'w'))
    println("Plain field size: " +  howManyOfType(dataMap, 'p'))
    println("Plain field size: " +  howManyOfType(dataMap, 'f'))
    assertEquals(dataMap.length, checkSize)
    assertEquals(dataMap.filter(arr => arr.length == checkSize).length, checkSize)
  }   
  def howManyOfType(dataM:Array[Array[Char]], aType:Char): Int = {
    var numb = 0
    for (arr <- dataM; e <- arr){
      //if(e != 'p' && e != 'w') println(e)
      if(e == aType) numb += 1
    }
    numb
  }
}