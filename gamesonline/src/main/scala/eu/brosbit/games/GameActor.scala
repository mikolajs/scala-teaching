package eu.brosbit.games

import akka.actor.Actor

class GameActor(gameId:String) extends Actor {
  val cookie = gameId
  var player1:PlayerLogged = null
  var player2:PlayerLogged = null
  val ticTacToe5 = new TicTacToe5("", PlayerLogged(), PlayerLogged())
  val userPoint:Array[Int] = new Array[Int](2)
   def receive = {
     case player:PlayerLogged => {
       if(player1 == null) {
         player1 = player
       }
       else if(player2 == null) {
         player2 = player
       }
     }
     case _ => println("Nothing")
   }
}
