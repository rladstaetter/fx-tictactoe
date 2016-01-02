package net.ladstatt.fx.ttt

import org.junit.Test
import org.junit.Assert._


/**
  * Created by lad on 01.01.16.
  */
class BruteForceTicTacToeStrategyTest {

  import BruteForceTicTacToeStrategy._

  @Test def testThatPlayFunctionReturnsGamesWhichEndedPrematurely(): Unit = {
    val game: TicTacToe = play(TicTacToe(), Seq(BottomLeft, BottomRight, MiddleLeft, MiddleRight, TopLeft, TopRight))
    assertEquals(
      """Winner is: Player A
        |o--
        |o-x
        |o-x
        |""".stripMargin, game.asString)
    assertEquals(5, game.movesSoFar.size)
  }

  @Test def testAllGames(): Unit = {
    assertEquals(1,allGames.size)

  }

  @Test def testGamesWithWinnerCount(): Unit = {
    assertEquals(1,gamesWithWinner.size)
  }

  @Test def testGamesWithDrawCount(): Unit = {

    println(gamesWithDraw.values.head.asString)
    assertEquals(1,gamesWithDraw.size)

    assertTrue(gamesWithDraw.forall {
      case (moves, t) =>
        println("asdfasdf")
        println(t.asString)
        t.isDraw
    })
  }

}
