package net.ladstatt.fx.ttt

/**
  * Created by lad on 31.12.15.
  */
trait TicTacToeStrategy {

  /**
    * returns the next turn for a given tic tac toe game, or none if the tic tac toe
    * game is already over, meaning somebody won or there is a draw
    *
    * @param game
    * @return
    */
  def calcNextTurn(game: TicTacToe): Option[TMove]

}
