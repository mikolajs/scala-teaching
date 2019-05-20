package eu.brosbit

import cats.instances.string._
import cats.syntax.semigroup._



object Main extends App {
  println("Hello " |+| "Cats!")
  OrderEssentialScalaExample.perform()
  EqaulityEssantialScalaExample.perform()
  EnrichmentImplicitEssentialScalaExample.perform()
  PrintableLibCatsBook.perform
  PrintableWithShowCatsBook.perform
//  def printPerson(p:Person ) = p match {
//    case Person(n, a) => s"Użytkownik: ${n} w wieku ${a}"
//    case _ => "Nieznany użytkownik"
//  }
//  def toTuple(tup:(String, Int)) = s"Name: ${tup._1} age: ${tup._2}"
//  import Person._
//
//  toTuple(Person("Jan Kowalski", 56))
//  printPerson(("Jan Kowalski", 56))
}
