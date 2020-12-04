package eu.brosbit

import eu.brosbit.games.{GameMove, PlayerLogged, TicTacToe5}
import org.scalatest.{FlatSpec, Matchers}

class TicTacToeSpec extends FlatSpec with Matchers {
  val player1 = PlayerLogged("1234", "graczA")
  val player2 = PlayerLogged("5678", "graczB")
  val ticTacToe5 = new TicTacToe5("123456", player1, player2)


  "TicTacToe" should "have 60×60 pool all with dot" in {
    ticTacToe5.pools.flatten.length shouldEqual 3600
    ticTacToe5.pools.flatten.forall(p => p == '.') shouldEqual true
  }

  it should "have initial setting" in {
    ticTacToe5.gameNumb shouldEqual 0
    ticTacToe5.ps1.move  shouldEqual true
    ticTacToe5.ps2.move shouldEqual false
    ticTacToe5.ps1.s shouldEqual  'o'
    ticTacToe5.ps2.s shouldEqual 'x'
    ticTacToe5.ps1.code shouldEqual "1234"
    ticTacToe5.ps2.code shouldEqual "5678"
  }

  it should "set 1 for o and 2 for x element" in {
    val info1 = ticTacToe5.mkMove(player1.code, 5, 5)
    val info2 = ticTacToe5.mkMove(player2.code, 4, 5)
    ticTacToe5.pools(5)(5) shouldEqual 'o'
    ticTacToe5.pools(4)(5) shouldEqual 'x'
    info1 shouldEqual("next")
    info2 shouldEqual("next")
  }

  it should "send error massage when its not turn of player" in {
    val infoWrong1 = ticTacToe5.mkMove(player2.code, 6, 6)
    val infoOK = ticTacToe5.mkMove(player1.code, 6, 6)
    val infoWrong2 = ticTacToe5.mkMove(player1.code, 7, 7)
    ticTacToe5.pools(6)(6) shouldEqual 'o'
    ticTacToe5.pools(7)(7) shouldEqual '.'
    infoWrong1 shouldEqual("wrong user")
    infoOK shouldEqual "next"
    infoWrong2 shouldEqual("wrong user")
  }

  it should "send error when player want to add busy place" in {
    val infoWrong = ticTacToe5.mkMove(player2.code, 6, 6)
    infoWrong shouldEqual("wrong place")
  }
  it should "and after proper place is OK" in {
    val infoOk = ticTacToe5.mkMove(player2.code, 4, 6)
    ticTacToe5.pools(4)(6) shouldEqual 'x'
    infoOk shouldEqual("next")

  }

  it should "if append o and x not 5 in line no win" in {
    val infoNext1 = ticTacToe5.mkMove(player1.code, 7, 7)
    val infoNext2 = ticTacToe5.mkMove(player2.code, 3, 4)
    val infoNext3 = ticTacToe5.mkMove(player1.code, 8, 8)
    val infoNext4 = ticTacToe5.mkMove(player2.code, 2, 4)
    infoNext1 shouldEqual("next")
    infoNext2 shouldEqual("next")
    infoNext3 shouldEqual("next")
    infoNext4 shouldEqual("next")
  }

  it should "" in {
    val infoLast1 = ticTacToe5.checkLastMove("1234")
    val infoLast2 = ticTacToe5.checkLastMove("5678")
    val infoLast3 = ticTacToe5.checkLastMove("4567")
    infoLast1 shouldEqual s"""{"check":"5678", "effect":"next", "row":2, "col":4 }"""
    infoLast2 shouldEqual s"""{"check":"5678", "effect":"next", "row":2, "col":4 }"""
    infoLast3 shouldEqual  s"""{"check":"NO",  "error":"not your game"}"""
  }

  it should "append 1 or 2 while add x or y on proper place with 5 inline make win" in {
    val infoNext1 = ticTacToe5.mkMove(player1.code, 9, 9)
    infoNext1 shouldEqual("won")
    ticTacToe5.gameNumb shouldEqual 1
    ticTacToe5.ps1.move shouldEqual false
    ticTacToe5.ps2.move shouldEqual true
  }

  it should "next game match must start player x (player2) not o" in {
    val infoWrong1 = ticTacToe5.mkMove(player1.code, 30, 30)
    val infoNext1 = ticTacToe5.mkMove(player2.code, 30, 30)
    infoNext1 shouldEqual("next")
    infoWrong1 shouldEqual("wrong user")
  }
}
