package pl.edu.osp

import akka.actor.{Props, ActorRef, Actor}

case class Stop()
case class Next()
case class Result()

class PrimeMainActor(many:Int) extends Actor  {
  val max = 15*many
  val ref:Array[ActorRef] = new Array[ActorRef](15)
  val primesCont = new Array[Int](15)
  var cheef:ActorRef = null
  for(i <- 0 to 14) ref(i) = context.actorOf(Props(classOf[PrimeActor], i, (i+1)*many))
    def receive = {
      case n:Int => {
        ref(0) ! n
      }
      case (n:Int, id:Int) => {
        if(id < 14) ref(id +1) ! n
      }
      case Counted(n, id) => {
        primesCont(id) = n
        println(s"Size of $id is $n")
        if(id == 14) {
          ref.foreach(r => context.stop(r))
          cheef ! primesCont
          mkStop()
        }
      }
      case Result => {
        cheef = sender()
        println("Receive Result")
      }
    }
  override def postStop(): Unit = {
    println("Main stopped")
  }

  def mkStop(): Unit = {
    println("Terminate actors system")
    context.system.terminate()
    context.stop(self)
  }
}
