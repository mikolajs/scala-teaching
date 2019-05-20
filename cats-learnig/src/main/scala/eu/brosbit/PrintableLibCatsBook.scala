package eu.dgt

object PrintableLibCatsBook {
  trait Printable[A] {
    def formating(a:A):String
  }

  object PrintableInstances {

    implicit object PrintableStr extends Printable[String] {
      def formating(a: String): String = "OTO: " + a
    }

    implicit object PrintableInt extends Printable[Int] {
      def formating(a: Int): String = "NUM: " + a
    }

    object Printable {
      implicit class PrintableClass[A](a: A) {
        def formating(implicit printable: Printable[A]) = printable.formating(a)

        def print(implicit printable: Printable[A]) = println(printable.formating(a))
      }
    }


  }


  def perform() {
    import  PrintableInstances._
    import Printable._
    println("Napis".formating)
    "Napis".print
     println(123.formating)
    123.print
  }

}
