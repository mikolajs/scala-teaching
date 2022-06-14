package eu.brosbit.games

import java.util.Date

case class PlayerSign(code:String, s:Char, var move:Boolean, var points:Int = 0, var last: Long = new Date().getTime)

class TicTacToe5(roomCode:String, player1:PlayerLogged, player2:PlayerLogged) {

  private val pools = Array.ofDim[Char](60, 60).map(_.map(_ => '.'))
///todo: make all methods, create for console test
  private val ps1 = PlayerSign(player1.code, 'o', move = true)
  private val ps2 = PlayerSign(player2.code, 'x', move = false)
  private var lastMove = (-1, -1)
  private var lastWon = false
  private var lastUser = ""
  private var gameNumb = 0
  private val lasting = 200000L

  def mkMove(userCode:String, row:Int, col:Int):String = {
    val ps = if(ps1.move) ps1 else ps2
    if(userCode != ps.code) return "wrong user"
    if(pools(row)(col) != '.')  return "wrong place"
    pools(row)(col) = ps.s
    lastUser = userCode
    lastMove = (row, col)
    if(userCode == ps1.code) ps1.last = new Date().getTime
    else ps2.last = new Date().getTime
    if(checkIfIs5(row, col)) {
      lastWon = true
      if(ps1.code == userCode) {
        ps1.points += 1
        player1.points = ps1.points
      } else {
        ps2.points += 1
        player2.points = ps2.points
      }
      mkWin()
      "won"
    } else {
      lastWon = false
      ps1.move = !ps1.move
      ps2.move = !ps2.move
      "next"
    }
  }

  def checkLastMove(userCode:String):String = {
    if(userCode != ps1.code && userCode != ps2.code) return s"""{"game":"NO",  "error":"not your game"}"""
    if(userCode == ps1.code) ps1.last = new Date().getTime else ps2.last = new Date().getTime
    val nextUser = if(ps1.move) ps1.code else ps2.code
    if(lastWon){
      lastMove = (-1, -1)
      lastWon = false
    }
    s"""{"game":"OK", "lastuser":"$lastUser", "row":${lastMove._1}, "col":${lastMove._2},
       |"nextuser":"$nextUser" , "turn":$gameNumb }""".stripMargin
  }

  def checkHealth():Boolean = {
    val now = new Date().getTime
    if(ps1.code == "" && ps2.code == "") false
    else if(ps1.code == "") (now - ps2.last) < lasting
    else if(ps2.code == "") (now - ps1.last < lasting)
    else (now - ps2.last) < lasting && (now - ps1.last < lasting)
  }

  private  def mkWin(): Unit ={
    gameNumb += 1
    if(gameNumb % 2 == 0){
      ps1.move = true
      ps2.move = false
    } else {
      ps1.move = false
      ps2.move = true
    }
    for(i <- 0 until 60; j <- 0 until 60) pools(i)(j) = '.'
  }

  private def checkIfIs5(row:Int, col:Int):Boolean =  {
    val elem:Char = pools(row)(col)
    //0° i 180°
    var r: Int = row - 1
    var c: Int = col
    var size = 1
    while (r > -1 && pools(r)(c) == elem) {
      size += 1
      r -= 1
    }
    r = row + 1
    while (r < 60 && pools(r)(c) == elem) {
      size += 1
      r += 1
    }
    if (size >= 5) {
      println("player " + elem + " won!")
      return true
    }
    //270° i 90°
    r = row
    c = col - 1
    size = 1
    while (c > -1 && pools(r)(c) == elem) {
      size += 1
      c -= 1
    }
    c = col + 1
    while (c < 60 && pools(r)(c) == elem) {
      size += 1
      c += 1
    }
    if (size >= 5) {
      println("player " + elem + " won!")
      return true
    }
    //315° i 135°
    r = row - 1
    c = col - 1
    size = 1
    while (c > -1 && r > -1 && pools(r)(c) == elem) {
      size += 1
      c -= 1
      r -= 1
    }
    c = col + 1
    r = row + 1
    while (c < 60 && r < 60 && pools(r)(c) == elem) {
      size += 1
      c += 1
      r += 1
    }
    if (size >= 5) {
      println("player " + elem + " won!")
      return true
    }
    //225° i 45°
    r = row + 1
    c = col - 1
    size = 1
    while (c > -1 && r < 60 && pools(r)(c) == elem) {
      size += 1
      c -= 1
      r += 1
    }
    c = col + 1
    r = row - 1
    while (c < 60 && r > -1 && pools(r)(c) == elem) {
      size += 1
      c += 1
      r -= 1
    }
    if (size >= 5) {
      //println("player " + elem + " won!")
      return true
    }
    false
  }

}
