package net.ladstatt.fx.ttt

import scala.util.Random

/**
  * A very naive way to implement a brute force strategy for tic tac toe
  */
object BruteForceTicTacToeStrategy extends TicTacToeStrategy {

  val newGame: TicTacToe = TicTacToe()

  /**
    * for a given list of moves, this function reduces the moves and the
    * starting state (the tic tac toe) to a resulting tic tac toe. If the
    * game is already over, the remaining moves are ignored.
    *
    * @param t
    * @param moves
    * @return
    */
  def play(t: TicTacToe, moves: Seq[TMove]): TicTacToe = {
    moves.foldLeft(t) {
      case (game, move) =>
        if (!game.isOver) {
          game.turn(move)
        } else game
    }
  }

  // calculate all tic tac toe games possible
  lazy val allGames: Map[Seq[TMove], TicTacToe] = {
    TicTacToe.allMoves.toSeq.permutations.map {
      case moves =>
        val t = play(newGame, moves)
        (t.movesSoFar, t)
    }.toMap
  }

  // separate draw games from games where a winner exists
  lazy val (gamesWithWinner: Map[Seq[TMove], TicTacToe], gamesWithDraw: Map[Seq[TMove], TicTacToe]) =
    allGames.partition {
      case (_, game) => game.winner.isDefined
    }

  // partition all games where a winner exists
  lazy val (gamesA, gamesB) = gamesWithWinner.partition {
    case (_, game) => game.winner.get._1 == PlayerA
  }

  /**
    * returns the next turn for a given tic tac toe game, or none if the tic tac toe
    * game is already over, meaning somebody won or there is a draw
    *
    * @param game
    * @return
    */
  override def calcNextTurn(game: TicTacToe): Option[TMove] = {
    if (game.isOver) None
    else {
      val plr = game.nextPlayer
      // only interested in the win for the player
      val winningGames: Map[Seq[TMove], TicTacToe] =
        if (plr == PlayerA) gamesA else gamesB

      // we try to win, so lets search if there exists a path to a winning game
      val potentialWinningMoves: Seq[Seq[TMove]] =
        winningGames.filter {
          case (moves, _) => moves.startsWith(game.movesSoFar)
        }.keys.toSeq

      if (potentialWinningMoves.nonEmpty) {
        // println(plr + ": " + potentialWinningMoves.size + " ways to to win the game.")
        Some(determineMove(game, potentialWinningMoves))
      } else {
        // check if we can reach a draw
        val potentialDraws =
          gamesWithDraw.filter {
            case (moves, _) => moves.startsWith(game.movesSoFar)
          }.keySet.toSeq
        if (potentialDraws.nonEmpty) {
          //println(plr + ": " + potentialDraws.size + " ways to a draw left.")
          Some(determineMove(game, potentialDraws))
        } else {
          // no winning path nor draw could be found, we'll loose
          // so just take any random move
          //println(plr + ": I take a random move since it is already clear I loose.")
          Some(game.remainingMoves.toSeq(Random.nextInt(game.remainingMoves.size)))
        }

      }
    }
  }

  /**
    * uses some heuristics to choose the best move.
    *
    * @param game
    * @param potentialMoves
    * @return
    */
  def determineMove(game: TicTacToe, potentialMoves: Seq[Seq[TMove]]): TMove = {
    println(game.asString)
    val winningMoveForOpponent = game.lookAhead(PlayerA)
    println("lookahead:" + winningMoveForOpponent)
    if (winningMoveForOpponent.isDefined) {
      winningMoveForOpponent.get
    } else {
      // we take the shortest path to win
      val possibilities = potentialMoves.sortWith((a, b) => a.size < b.size)
      val aPathToWin = possibilities.head

      // remove already moved turns
      //val moves: Seq[TMove] =
      val nextMoves: Seq[TMove] = aPathToWin.drop(game.movesSoFar.length)

      val t = nextMoves.head
      println(game.nextPlayer + ": took " + t + " as a move.")
      t
      //moves(Random.nextInt(moves.size)) // take a random next step out of the possible wins
    }
  }
}
