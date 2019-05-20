package eu.dgt

//import scala.language.implicitConversions
case class Person(name:String, age:Int){
  implicit def asTuple:(String, Int) = (name, age)
}

object PersonImplicity {
}
