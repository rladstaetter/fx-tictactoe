package net.ladstatt.fx.ttt

import scala.util.Random

/**
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
