package net.brosbit

case class Person(fName:String, lName:String)

class UsingName {
  val person = Person("Mikołaj", "Sochacki")
  given Person = person
  def chekUsing(age:Int)(using p:Person) = s" Twój wiek ${p.fName} ${p.lName} to $age"

}
