package eu.dgt

object EnrichmentImplicitEssentialScalaExample {
  object RichIntObj {

    implicit class RichInt(i: Int) {
      def yeah = "O yeah " + i
      def times = ((0 to i).toList.map(x => s"Look number ${x}")).mkString("\n")
    }

  }

  object EqualImplicit {
    implicit val lowerCaseEqualString = new Equal[String] {
      def equal(s1:String, s2:String) = s1.toLowerCase == s2.toLowerCase
    }
  }

  trait Equal[A]{
    def equal(a1:A, a2:A):Boolean
  }

  object Equal {
    def apply[A](implicit equal:Equal[A]) = equal
    implicit class ToEqual[A](a:A){
      def ===(other:A)(implicit equal:Equal[A]) = equal.equal(a, other)
    }
  }

  def perform(): Unit ={
    import RichIntObj._
    println(22.yeah)
    println(-1.yeah)
    println(4.times)
    import  Equal._
    import EqualImplicit._
    println("abcd".===("ABCD"))
  }
}
