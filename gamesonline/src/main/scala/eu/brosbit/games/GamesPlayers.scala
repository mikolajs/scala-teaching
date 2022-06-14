package eu.brosbit.games

import java.util.{Date, UUID}

case class PlayerLogged(code: String = "", name:String = "", expires:Long = 0, var points:Int = 0) {
  def jsonStr = s"""{"code":"$code", "name":"$name", "expires":"$expires", "points":$points}"""
}

case class RoomInfo(roomCode:String, player1:PlayerLogged, player2:PlayerLogged, game:Boolean = false) {
  def jsonStr:String =
    s"""{"roomCode":"$roomCode", "player1":${if(player1 != null) player1.jsonStr else PlayerLogged().jsonStr},
       |"player2":${if(player2 != null) player2.jsonStr else PlayerLogged().jsonStr} }""".stripMargin
}

case class  GameMove(row:Int, col:Int)

object GamesPlayers {

  private val loggedIn = collection.mutable.Map[String, PlayerLogged]() // code, name
  private val rooms = collection.mutable.Map[String, RoomInfo]()
  private val games = collection.mutable.Map[String, TicTacToe5]()


  def mkLogin(userName:String, points:Int):String = {
    loggedIn.find(p => p._2.name == userName) match {
      case Some((_, ps)) =>
        loggedIn.put(ps.code, PlayerLogged(ps.code, ps.name, new Date().getTime+72000000L, points))
        ps.code
      case _ =>  {
        val code = UUID.randomUUID().toString
        loggedIn += (code -> PlayerLogged(code, userName, new Date().getTime+72000000L,  points))
        code
      }
    }
  }

  def mkLogout(code:String):Option[String] = {
    clearRoom(code)
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
      var room = rooms(roomCode)
      if(room.player1 != null && room.player2 != null) return Some(""" {"add":"NO", "error":"already full"} """)
      if(room.player1 == null) rooms.put(roomCode, RoomInfo(roomCode, user, room.player2))
      else rooms.put(roomCode, RoomInfo(roomCode, room.player1, user))
      room = rooms(roomCode)
      if(!room.game) {
        games.put(roomCode, new TicTacToe5(roomCode, room.player1, room.player2))
        rooms.put(roomCode, RoomInfo(roomCode, room.player1, room.player2, game = true))
      }
    } else {
      clearRoom(userCode) //delete old room
      val newRoomCode = UUID.randomUUID().toString
      rooms.put(newRoomCode, RoomInfo(newRoomCode, user, null))
      return Some(s""" {"add":"OK", "roomCode":"$newRoomCode"} """)
    }
    Some(s""" {"add":"OK", "roomCode":"$roomCode"} """)
  }


  def checkRoom(userCode: String, roomCode: String): Option[String] = {
    if(!loggedIn.isDefinedAt(userCode)) return Some(""" {"check":"NO", "info":"not logged"} """ )
    if(!rooms.isDefinedAt(roomCode)) return Some(""" {"check":"NO", "info":"room not exists"} """ )
    val room = rooms(roomCode)
    if((room.player1 != null && room.player1.code != userCode) && (room.player2 != null && room.player2.code != userCode))
        return Some(""" {"check":"NO", "info":"its not your room"} """)

    val move = if(games.isDefinedAt(roomCode)) games(roomCode).checkLastMove(userCode) else """ {"game":"NO", "info":"Not started yet"} """
    Some(s""" {"check":"OK", "roomInfo":${room.jsonStr}, "move":$move} """)
  }

  def nextMove(userCode: String, roomCode: String, row: Int, col: Int): Option[String] = {
    if(!loggedIn.isDefinedAt(userCode)) return Some(""" {"add":"NO", "info":"not logged"} """ )
    if(!rooms.isDefinedAt(roomCode)) return Some(""" {"add":"NO", "info":"room not exists"} """ )
    val room = rooms(roomCode)
    if(room.player1 == null || room.player2 == null) return Some(""" {"add":"NO", "info":"second player not in game"} """)
    if(room.player1.code != userCode && room.player2.code != userCode)
      return Some(""" {"add":"NO", "info":"its not your room"} """)

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
    if(room.player1.code != userCode && room.player2.code != userCode)
      return Some(""" {"check":"NO", "info":"its not your room"} """)
    if(!room.game)  return Some(""" {"check":"NO", "info":"not started yet"} """)

    val game = games(roomCode)
    val info = game.checkLastMove(userCode)
    Some(info)
  }

  def clearDysfunctionalRooms() = {
    val keys = games.keys
    keys.foreach(key =>{
      if(!games(key).checkHealth()) {
        games.remove(key)
        rooms.remove(key)
      }
    })
  }
  def clearRoom(userCode:String): Unit = {
    val toClearRooms = rooms.filter(room => {
      (room._2.player1 != null && room._2.player1.code == userCode) ||
        (room._2.player2 != null && room._2.player2.code == userCode)} )
    toClearRooms.foreach(cRooms =>
      if(rooms.isDefinedAt(cRooms._1)) {
        rooms.remove(cRooms._1)
        games.remove(cRooms._1)
      }
    )
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
