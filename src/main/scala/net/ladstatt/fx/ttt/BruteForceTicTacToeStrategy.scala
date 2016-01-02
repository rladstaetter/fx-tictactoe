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
  lazy val allGames: Map[collection.Set[TMove], TicTacToe] = {
    TicTacToe.allMoves.toSeq.permutations.map {
      case moves =>
        val t = play(newGame, moves)
        (t.movesSoFar, t)
    }.toMap
  }

  // separate draw games from games where a winner exists
  lazy val (gamesWithWinner: Map[collection.Set[TMove], TicTacToe], gamesWithDraw: Map[collection.Set[TMove], TicTacToe]) =
    allGames.partition {
      case (_, game) => game.winner.isDefined
    }

  lazy val possibleDraws: Map[collection.Set[TMove], TicTacToe] = gamesWithDraw.map {
    case (moves, g) => moves.toSet -> g
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
  override def nextTurn(game: TicTacToe): Option[TMove] = {
    if (game.isOver) None
    else {
      println("Player: " + game.nextPlayer)
      // only interested in the win for the player
      val winningGames: Map[collection.Set[TMove], TicTacToe] =
        (if (game.nextPlayer == PlayerA) gamesA else gamesB).map {
          case (moves, g) => moves.toSet -> g
        }
      println("gamesA:" + gamesA.size)
      println("gamesB:" + gamesB.size)
      println("draws:" + gamesWithDraw.size)
      println("games:" + winningGames.size)
      // we try to win, so lets search if there exists a path to a winning game
      val possibleWins =
        winningGames.filter {
          case (moves, _) => game.movesSoFar.subsetOf(moves)
        }.keySet

      if (possibleWins.nonEmpty) {
        Some(determineMove(game, possibleWins))
      } else {
        // check if we can reach a draw
        val possibleDs =
          possibleDraws.filter {
            case (moves, _) => game.movesSoFar.subsetOf(moves)
          }.keySet
        if (possibleDraws.nonEmpty) {
          Some(determineMove(game, possibleDs))
        } else {
          // no winning path nor draw could be found, we'll loose
          // so just take any random move
          Some(game.remainingMoves.toSeq(Random.nextInt(game.remainingMoves.size)))
        }

      }
    }
  }

  /**
    * uses some heuristics to choose the best move.
    *
    * @param game
    * @param availableMoves
    * @return
    */
  def determineMove(game: TicTacToe, availableMoves: Set[collection.Set[TMove]]): TMove = {
    // we take the shortest path to win
    val possibilities = availableMoves.toSeq.sortWith((a, b) => a.size > b.size)
    val aPathToWin = possibilities.head
    println(aPathToWin)
    // remove already moved turns
    val moves: Seq[TMove] = (aPathToWin -- game.movesSoFar).toSeq
    moves(Random.nextInt(moves.size)) // take a random next step out of the possible wins
  }
}
