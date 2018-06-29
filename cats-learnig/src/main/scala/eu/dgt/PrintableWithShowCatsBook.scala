package eu.dgt

import java.util.Date
import cats.instances.string._
import cats.instances.int._
import cats.Show
import cats.Eq
import cats.syntax.show._


case class Fly (where:String, date:Date)

object PrintableWithShowCatsBook {
  object MyCatsExtention {
    implicit val flyShow = Show.show[Fly](f =>
    s"The fly to: ${f.where.show} is out at: ${f.date.toString}")

    implicit val eqFly = Eq.instance[Fly]((f1, f2) => f1.where == f2.where)
  }

  def perform(): Unit = {
    println("PRINTABLE WITH CATS")
    println(Show[Int].show(333))
    println(124.show)
    val fly = Fly("Naples", new Date())
    import MyCatsExtention._
    println(fly.show)
    val fly2 = Fly("Naples", new Date())
    import cats.syntax.eq._
    println(fly === fly2)
  }

}
