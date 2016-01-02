package net.ladstatt.fx.ttt

import org.junit.Assert._
import org.junit.Test

import scala.collection.immutable.ListMap

/**
  * Tests the tic tac toe game engine.
  */
class TicTacToeTest {


  /**
    * contains all possible games as keys and the according game.
    */
  lazy val allGames: Map[Seq[TMove], TicTacToe] = TicTacToe.mkGames()

  @Test def instantiateAnEmptyTicTacToeGame(): Unit = {
    assertNotNull(TicTacToe())
  }

  @Test def createEmptyGameAndTurnOnceCheckRemainingMoves(): Unit = {
    val emptyNewGame: TicTacToe = TicTacToe()
    val t: TicTacToe = emptyNewGame.turn(TopCenter, PlayerA)
    assertEquals(emptyNewGame.remainingMoves.size - 1, t.remainingMoves.size)
    assertEquals(PlayerB, t.nextPlayer)
  }


  @Test def newGameIsNotOverYet(): Unit = {
    assertFalse(TicTacToe().isOver)
  }

  @Test def newGameHasNoWinner(): Unit = {
    assertTrue(TicTacToe().winner.isEmpty)
  }

  @Test def newGameHasNoDraw(): Unit = {
    assertFalse(TicTacToe().isDraw)
  }

  private val gamePlayerAWins: TicTacToe = TicTacToe(
    ListMap() ++ Map(
      TopLeft -> PlayerA,
      TopCenter -> PlayerA,
      TopRight -> PlayerA,
      MiddleLeft -> PlayerB,
      MiddleCenter -> PlayerB
    )
  )

  @Test def playerAWinsTheGame(): Unit = {
    assertTrue(gamePlayerAWins.winner.isDefined)
    assertEquals(PlayerA, gamePlayerAWins.winner.get._1)

  }

  @Test def playerBWinsTheGame(): Unit = {
    val game = TicTacToe(
      ListMap() ++ Map(
        TopLeft -> PlayerB,
        TopCenter -> PlayerB,
        TopRight -> PlayerB,
        MiddleLeft -> PlayerA,
        MiddleCenter -> PlayerA
      )
    )
    assertTrue(game.winner.isDefined)
    assertEquals(PlayerB, game.winner.get._1)
    assertTrue(game.isOver)
    assertTrue(!game.isDraw)

  }

  @Test def thereIsADraw(): Unit = {
    val game = TicTacToe(
      ListMap() ++ Map(
        TopLeft -> PlayerA,
        TopCenter -> PlayerB,
        TopRight -> PlayerA,
        MiddleLeft -> PlayerB,
        MiddleCenter -> PlayerB,
        MiddleRight -> PlayerA,
        BottomLeft -> PlayerB,
        BottomCenter -> PlayerA,
        BottomRight -> PlayerB
      )
    )
    assertTrue(game.winner.isEmpty)
    assertTrue(game.isDraw)
    assertTrue(game.isOver)

  }


  @Test def testShowsThatPlayerAHasWonAndGameIsOver(): Unit = {
    val gameOver = gamePlayerAWins.isOver
    assertTrue(gameOver)
  }

  /**
    * Test shows that when you start a tic tac toe game, you'd have
    * 9 possible moves.
    */
  @Test def testShowsOpeningRound(): Unit = {
    val openingGames = TicTacToe().nextGames
    assertEquals(9, openingGames.size)

    openingGames.foreach {
      case g =>
        assertEquals(8, g.remainingMoves.size)
        assertEquals(1, g.movesSoFar.size)
    }
  }

  @Test def testAsString(): Unit = {
    assertEquals(
      """---
        |---
        |---
        | """.stripMargin, TicTacToe.asString(TicTacToe().stateMap))
  }


  @Test def testAsString2(): Unit = {
    assertEquals(
      """ooo
        |xx-
        |---
        | """.stripMargin, TicTacToe.asString(gamePlayerAWins.stateMap))
  }

  @Test def testAsString3(): Unit = {
    assertEquals(
      """Winner is: Player A
        |ooo
        |xx-
        |---
        | """.stripMargin, gamePlayerAWins.asString)
  }

  // implement yourself more tests

}




