package eu.dgt


object OrderEssentialScalaExample {

  object OrderByUnits {
    implicit val byUnits = Ordering.fromLessThan[Order](_.units < _.units)
  }

  object OrderByUnitPrice {
    implicit val byUnitPrice = Ordering.fromLessThan[Order](_.unitPrice < _.unitPrice)
  }

  object OrderByTotal {
    implicit val byTotal = Ordering.fromLessThan[Order](_.totalPrice < _.totalPrice)
  }

  final case class Order(units: Int, unitPrice: Double) {
    val totalPrice: Double = units * unitPrice
  }

  val orders = (1 to 10).toList.map(i => Order((Math.random() * 20.0).asInstanceOf[Int] + 1, Math.random() * 100.0))

  def perform() {
    {
      import OrderByUnits._
      println(orders.sorted)
    }
    {
      import OrderByUnitPrice._
      println(orders.sorted)
    }
    {
      import OrderByTotal._
      println(orders.sorted)
    }

  }

}
