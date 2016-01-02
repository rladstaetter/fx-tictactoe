package net.ladstatt.fx.ttt

import org.junit.Assert._
import org.junit.Test


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
        |o-x""".stripMargin, game.asString)
    assertEquals(5, game.movesSoFar.size)
  }

  @Test def testAllGames(): Unit = {
    assertEquals(255168, allGames.size)
  }

  @Test def testGamesWithWinnerCount(): Unit = {
    assertEquals(209088, gamesWithWinner.size)
  }

  @Test def testGamesWithDrawCount(): Unit = {

    assertEquals(46080, gamesWithDraw.size)

    assertTrue(gamesWithDraw.forall {
      case (moves, t) => t.isDraw
    })
  }

  /**
    * in this game situation, for player 'x' there is only one chance to block
    * player 'o' from winning: placing the next move on TopCenter.
    */
  @Test def testScenario1(): Unit = {
    val g = TicTacToe(
      """---
        |-o-
        |xo-""".stripMargin)
    assert(!g.isOver)
    calcNextTurn(g) match {
      case None => fail()
      case Some(m) =>
        assertEquals(TopCenter, m)
        assertEquals(PlayerB, g.nextPlayer)
        val nextGame: TicTacToe = g.turn(m)
        assertFalse(nextGame.winner.isDefined)
    }
  }

  @Test def testWinningMoveForOpponent(): Unit = {
    val g = TicTacToe(
      """---
        |-o-
        |xo-""".stripMargin)
    assertFalse(g.turn(TopCenter, PlayerA).isDraw)
    assertTrue(g.turn(TopCenter, PlayerA).winner.isDefined)
    g.lookAhead(PlayerA) match {
      case None => fail()
      case Some(m) => assertEquals(TopCenter, m)
    }

  }


}
