package eu.brosbit.games

import java.util.{Date, UUID}

import eu.brosbit.NewUserCreate
import eu.brosbit.games.GamesPlayers.rooms


case class PlayerLogged(code: String = "", name:String = "", expires:Long = 0, points:Int = 0) {
  def jsonStr = s"""{"code":"$code", "name":"$name", "expires":"$expires", "points":$points}"""
}

case class RoomInfo(roomCode:String, player1:PlayerLogged, player2:PlayerLogged, game:Boolean = false) {
  def jsonStr =
    s"""{"roomCode":"$roomCode", "player1":${if(player1 != null) player1.jsonStr else PlayerLogged().jsonStr},
       |"player2":${if(player2 != null) player2.jsonStr else PlayerLogged().jsonStr} }""".stripMargin
}

case class  GameMove(row:Int, col:Int)

object GamesPlayers {

  val loggedIn = collection.mutable.Map[String, PlayerLogged]() // code, name
  val rooms = collection.mutable.Map[String, RoomInfo]()
  val games = collection.mutable.Map[String, TicTacToe5]()


  def mkLogin(userName:String, points:Int):String = {
    val code = UUID.randomUUID().toString
    loggedIn.find(p => p._2.name == userName) match {
      case Some((c, ps)) => loggedIn.put(code, PlayerLogged(code, ps.name, new Date().getTime+72000000L, points))
      case _ =>  loggedIn += (code -> PlayerLogged(code, userName, new Date().getTime+72000000L,  points))
    }
    code
  }

  def mkLogout(code:String):Option[String] = {
    loggedIn.remove(code).map(_.name)
  }

  def isLogged(code: String):Boolean = {
    loggedIn.isDefinedAt(code)
  }

 @deprecated
  def getLoggedUser(code:String):Option[String] = {
    if(isLogged(code)) {
      val str = loggedIn.toList.map( _._2.jsonStr).mkString(", ")
      Some(s"""{"users": [$str]}""")
    } else {
      None
    }
  }

  def getAllPlayers(code:String):Option[String] = {
    if(isLogged(code)) {
      val logged = loggedIn.toList.map( _._2.jsonStr).mkString(", ")
      val inRooms = rooms.toList.map(roomInfo => roomInfo._2.jsonStr ).mkString(", ")
      Some(s"""{"users": [$logged], "rooms":[$inRooms]}""")
    } else None
  }

  def getUserInfo(code:String):Option[String] = {
    if(loggedIn.isDefinedAt(code)) {
      Some(loggedIn(code).jsonStr)
    } else None
  }

  def addUserToRoom(userCode:String, roomCode:String):Option[String] = {
    if(!loggedIn.isDefinedAt(userCode)) return Some(""" {"add":"NO", "error":"not logged"} """ )
    val roomOpt = rooms.find(room => (room._2.player1 != null && room._2.player1.code == userCode) ||
      (room._2.player2 != null && room._2.player2.code == userCode))
    if(roomOpt.isDefined && roomOpt.get._1 == roomCode) return Some(""" {"add":"NO", "error":"already in"} """)


    val user = loggedIn(userCode)
    if(rooms.isDefinedAt(roomCode)) {
      val room = rooms(roomCode)
      if(room.player1 == null) rooms.put(roomCode, RoomInfo(roomCode, user, room.player2, false))
      else if(room.player2 == null) rooms.put(roomCode, RoomInfo(roomCode, room.player1, user, false))
      else return Some(""" {"add":"NO", "error":"already full"} """)
    } else {
      val newRoomCode = UUID.randomUUID().toString
      rooms.put(newRoomCode, RoomInfo(newRoomCode, user, null, false))
      return Some(s""" {"add":"OK", "roomCode":"${newRoomCode}"} """)
    }
    Some(s""" {"add":"OK", "roomCode":"${roomCode}"} """)
  }


  def checkRoom(userCode: String, roomCode: String): Option[String] = {
    if(!loggedIn.isDefinedAt(userCode)) return Some(""" {"check":"NO", "error":"not logged"} """ )
    if(!rooms.isDefinedAt(roomCode)) return Some(""" {"check":"NO", "error":"room not exists"} """ )
    val room = rooms(roomCode)

    Some(s""" {"check":"OK", "roomInfo":${room.jsonStr}} """)


  }

  def nextMove(userCode: String, roomCode: String, row: Int, col: Int): Option[String] = {
    if(!loggedIn.isDefinedAt(userCode)) return Some(""" {"add":"NO", "info":"not logged"} """ )
    if(!rooms.isDefinedAt(roomCode)) return Some(""" {"add":"NO", "info":"room not exists"} """ )
    val room = rooms(roomCode)
    if(room.player1 == null || room.player2 == null) return Some(""" {"add":"NO", "info":"second player not in game"} """)
    if(room.player1.code != userCode && room.player2.code != userCode)
      return Some(""" {"add":"NO", "info":"its not your room"} """)
    if(room.game == false) {
      //clearNotWorkingGame(roomCode)
      games.put(roomCode, new TicTacToe5(roomCode, room.player1, room.player2))
      rooms.put(roomCode, RoomInfo(roomCode, room.player1, room.player2, true))
    }
    val game = games(roomCode)
    val info = game.mkMove(userCode, row, col)
    if(info == "next") Some(""" {"add":"OK", "info":"next"} """)
    else if(info == "won") {
      Some(""" {"add":"OK", "info":"won"} """)
    } else Some(s""" {"add":"NO", "info":"$info"} """)
  }

  def checkMove(userCode: String, roomCode: String): Option[String] = {
    if(!loggedIn.isDefinedAt(userCode)) return Some(""" {"check":"NO", "info":"not logged"} """ )
    if(!rooms.isDefinedAt(roomCode)) return Some(""" {"check":"NO", "info":"room not exists"} """ )
    val room = rooms(roomCode)
    if(room.player1 == null || room.player2 == null) return Some(""" {"check":"NO", "info":"second player not in game"} """)
    if(room.player1.code != userCode && room.player2.code != userCode)
      return Some(""" {"check":"NO", "info":"its not your room"} """)
    if(room.game == false)  return Some(""" {"check":"NO", "info":"not started yet"} """)

    val game = games(roomCode)
    val info = game.checkLastMove(userCode)
    Some(info)
  }

  ///todo: ???? i need clear working rooms with games when people don't play
  private def clearNotWorkingGame(roomCode: String): Unit ={
      val listRooms = rooms
  }

  private  def test(): Unit ={
    println("loggedIn contains: ")
    loggedIn.foreach(f => println(f._1 + " : " + f._2))
  }

}
