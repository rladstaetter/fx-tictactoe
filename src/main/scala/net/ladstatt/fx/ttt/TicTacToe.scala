package net.ladstatt.fx.ttt

import scala.collection.Set
import scala.collection.immutable.ListMap

/**
  * models the different moves the game allows
  *
  * each move is made by either player a or player b.
  */
sealed trait TMove

case object TopLeft extends TMove
case object TopCenter extends TMove
case object TopRight extends TMove

case object MiddleLeft extends TMove
case object MiddleCenter extends TMove
case object MiddleRight extends TMove

case object BottomLeft extends TMove
case object BottomCenter extends TMove
case object BottomRight extends TMove


/**
  * for a tic tac toe game, there are two players, player A and player B
  */
sealed trait Player {
  def asString: String
}

case object PlayerA extends Player {
  override def asString: String = "o"
}

case object PlayerB extends Player {
  override def asString: String = "x"
}



/**
  * Models the well known tic tac toe game.
  *
  * The map holds the information which player controls which field.
  *
  * The nextplayer parameter defines which player makes the next move.
  */
case class TicTacToe(moveHistory: ListMap[TMove, Player] = ListMap(), nextPlayer: Player = PlayerA) {

  lazy val stateMap: Map[TMove, String] = {
    TicTacToe.allMoves.map {
      case m => m -> moveHistory.get(m).map(_.asString).getOrElse("-")
    }.toMap
  }

  /**
    * outputs a representation of the tic tac toe
    *
    * @return
    */
  lazy val asString: String = {

    val header =
      if (isOver) {
        winner.map {
          case (p, _) => "Winner is: Player " + (if (p == PlayerA) "A" else "B")
        }.getOrElse("It's a draw.")
      } else ""

    header + "\n" + TicTacToe.asString(stateMap)
  }


  /**
    * is true if the game is over.
    *
    * The game is over if either of a player wins or there is a draw.
    */
  lazy val isOver: Boolean = winner.isDefined || isDraw

  /**
    * true if there is no winner
    */
  lazy val isDraw: Boolean = winner.isEmpty && remainingMoves.isEmpty

  /**
    * All fields which are taken so far by a player
    */
  lazy val movesSoFar: Set[TMove] = moveHistory.keySet

  /**
    * the moves which are still to be played on this tic tac toe game.
    */
  lazy val remainingMoves: Set[TMove] = TicTacToe.allMoves -- movesSoFar

  /**
    * Returns the set of all possible TicTacToe games for the next move.
    *
    * For example, when you start playing, you would have 9 possible TicTacToe games in
    * the set since you would have 9 ways to start the game. In the second round,
    * there would be 8 possibilities and so on.
    *
    */
  lazy val nextGames: Set[TicTacToe] = {
    remainingMoves.map {
      case m => turn(m, nextPlayer)
    }
  }

  /**
    * Either there is no winner, or PlayerA or PlayerB won the game.
    *
    * The set of moves contains all moves which contributed to the result.
    */
  lazy val winner: Option[(Player, Set[TMove])] = {

    val allMovesPlayerA: Set[TMove] =
      moveHistory.filter {
        case (move, player) => player == PlayerA
      }.keySet

    val allMovesPlayerB: Set[TMove] =
      moveHistory.filter {
        case (move, player) => player == PlayerB
      }.keySet

    if (TicTacToe.winners.exists(set => set.subsetOf(allMovesPlayerA))) {
      Some((PlayerA, Set[TMove]()))
    } else if (TicTacToe.winners.exists(set => set.subsetOf(allMovesPlayerB))) {
      Some((PlayerB, Set[TMove]()))
    } else None
  }

  /**
    * Returns a copy of the current game,
    * but with the move applied to the tic tac toe game.
    *
    * @param move   to be played
    * @param player the player
    * @return
    */
  def turn(move: TMove, player: Player = nextPlayer): TicTacToe = {
    copy(moveHistory = this.moveHistory ++ Map(move -> player),
      nextPlayer = if (nextPlayer == PlayerA) PlayerB else PlayerA)
  }

}

/**
  * Created by lad on 02.01.16.
  */
object TicTacToe {

  /**
    * All possible moves for a 3 x 3 Tic Tac Toe
    */
  val allMoves: Set[TMove] =
    Set(TopLeft, TopCenter, TopRight,
      MiddleLeft, MiddleCenter, MiddleRight,
      BottomLeft, BottomCenter, BottomRight)

  val win1: Set[TMove] = Set(TopLeft, TopCenter, TopRight)
  val win2: Set[TMove] = Set(MiddleLeft, MiddleCenter, MiddleRight)
  val win3: Set[TMove] = Set(BottomLeft, BottomCenter, BottomRight)
  val win4: Set[TMove] = Set(TopLeft, MiddleLeft, BottomLeft)
  val win5: Set[TMove] = Set(TopCenter, MiddleCenter, BottomCenter)
  val win6: Set[TMove] = Set(TopRight, MiddleRight, BottomRight)
  val win7: Set[TMove] = Set(TopLeft, MiddleCenter, BottomRight)
  val win8: Set[TMove] = Set(TopRight, MiddleCenter, BottomLeft)

  val winners: Set[Set[TMove]] =
    Set(win1, win2, win3, win4, win5, win6, win7, win8)


  /**
    * For a given tic tac toe game, this function applies all moves to the game.
    * The first element of the sequence is also the first move.
    *
    * @param t
    * @param moves
    * @return
    */
  def play(t: TicTacToe, moves: Seq[TMove]): TicTacToe = ???

  /**
    * creates all possible games.
    *
    * @return
    */
  def mkGames(): Map[Seq[TMove], TicTacToe] = ???

  def asString(t: TicTacToe): String = asString(t.stateMap)

  def asString(ticTacState: Map[TMove, String]): String = {
    s"${ticTacState(TopLeft)}${ticTacState(TopCenter)}${ticTacState(TopRight)}" + "\n" +
      s"${ticTacState(MiddleLeft)}${ticTacState(MiddleCenter)}${ticTacState(MiddleRight)}" + "\n" +
      s"${ticTacState(BottomLeft)}${ticTacState(BottomCenter)}${ticTacState(BottomRight)}" + "\n"
  }

}
