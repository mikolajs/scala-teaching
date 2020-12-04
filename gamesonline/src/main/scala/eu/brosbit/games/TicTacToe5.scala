package eu.brosbit.games

case class PlayerSign(val code:String, val s:Char, var move:Boolean)

class TicTacToe5(roomCode:String, player1:PlayerLogged, player2:PlayerLogged) {

  val pools = Array.ofDim[Char](60, 60).map(_.map(_ => '.'))
///todo: make all methods, create for console test
  val ps1 = PlayerSign(player1.code, 'o', true)
  val ps2 = PlayerSign(player2.code, 'x', false)
  var lastMove = (-1, -1)
  var lastWon = false
  var lastUser = ""
  var gameNumb = 0

  def mkMove(userCode:String, row:Int, col:Int):String = {
    val ps = if(ps1.move) ps1 else ps2
    if(userCode != ps.code) return "wrong user"
    if(pools(row)(col) != '.')  return "wrong place"
    pools(row)(col) = ps.s
    if(checkIfIs5(row, col)) {
      gameNumb += 1
      lastUser = userCode
        if(gameNumb % 2 == 0){
        ps1.move = true
        ps2.move = false
      } else {
        ps1.move = false
        ps2.move = true
      }
      pools.map(_.map(_ => '.'))
      lastMove = (row, col)
      lastWon = true

      "won"
    } else {
      lastWon = false
      lastUser = userCode
      ps1.move = !ps1.move
      ps2.move = !ps2.move
      lastMove = (row, col)
      "next"
    }
  }

  def checkLastMove(userCode:String):String = {
    if(userCode != ps1.code && userCode != ps2.code) return s"""{"check":"NO",  "error":"not your game"}"""
    val last = if(lastWon) "won" else "next"
    val whoMove = if(ps1.move) ps1.code else ps2.code
    if(userCode != whoMove)
      s"""{"check":"NO",  "user":"$lastUser", "effect":"wait"}"""
    else s"""{"check":"OK", "user":"$lastUser", "effect":"$last", "row":${lastMove._1}, "col":${lastMove._2} }"""
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
      println("player " + elem + " won!")
      return true
    }
    false
  }

}
