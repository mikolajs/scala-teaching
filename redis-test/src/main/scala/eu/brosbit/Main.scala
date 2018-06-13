package eu.brosbit

import com.redis._

object Main {
  def main(args:Array[String]){
    val redis = new RedisClient("localhost", 6379)
    println(redis.get("klucztestowy").getOrElse("Brak klucza"))
  }
}

