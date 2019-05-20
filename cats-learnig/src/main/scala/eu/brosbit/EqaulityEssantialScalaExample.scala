package eu.dgt



object EqaulityEssantialScalaExample {
  case class Person(name: String, email: String)
  trait Equal[A] {
    def equal(v1: A, v2: A): Boolean
  }

  object EqualImplicit {

    implicit object EmailEqual extends Equal[Person] {
      def equal(v1: Person, v2: Person): Boolean =
        v1.email == v2.email
    }

    implicit object NameEmailEqual extends Equal[Person] {
      def equal(v1: Person, v2: Person): Boolean =
        v1.email == v2.email && v1.name == v2.name
    }

  }
  object Eq {
    def apply[A](p1: A, p2:A)(implicit equal: Equal[A]) = equal.equal(p1, p2)
  }
  
  object Equal {
    def apply[A](implicit equal:Equal[A]) = equal
  }

  def perform(): Unit = {
    import EqualImplicit.EmailEqual
    println(Eq(Person("Noelowi", "noel@example.com"), Person("Noel", "noel@example.com")))
    println(Equal[Person].equal(Person("Noelowi", "noel@example.com"), Person("Noel", "noel@example.com")))
  }
}
