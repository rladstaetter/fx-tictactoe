package net.ladstatt.tictactoe

import scala.util.Random

/**
  * Another strategy which selects a random turn. Not very competitive, though.
  *
  * Created by lad on 31.12.15.
  */
object RandomTicTacToeStrategy extends TicTacToeStrategy {

  def calcNextTurn(game: TicTacToe): Option[TMove] = {
    if (game.isOver) None
    else {
      val moves: Seq[TMove] = game.remainingMoves.toSeq
      Some(moves(Random.nextInt(moves.size)))
    }
  }

}
