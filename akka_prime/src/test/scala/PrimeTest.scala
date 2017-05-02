package pl.brosbit

import org.scalatest._
import pl.edu.osp.PrimeBank

class PrimeTest extends FlatSpec with Matchers {

  "PrimeTest" should "Can check is prime and return correct value" in {
    val pb = new PrimeBank(4)
    pb.check(2L) should be (0L)
    pb.check(3L) should be (0L)
    pb.check(4L) should be (0L)
    pb.check(5L) should be (0L)
    pb.check(6L) should be (0L)
    pb.check(7L) should be (0L)
    pb.check(11L) should be (11L)
    pb.check(17L) should be (17L)
    pb.check(49L) should be (0L)
    pb.check(50L) should be (0L)
    pb.check(121L) should be (121L)
    pb.bank.size should be (4)
  }

  it should "can eliminate not prime number witch fatorial include bank" in {
    
  }
}