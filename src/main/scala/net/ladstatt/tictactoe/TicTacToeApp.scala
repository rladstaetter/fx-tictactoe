package net.ladstatt.tictactoe

import java.net.URL
import java.util.ResourceBundle
import javafx.application.Application
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXMLLoader, FXML, Initializable}
import javafx.scene.{Scene, Parent}
import javafx.scene.control.{Label, Button}
import javafx.stage.Stage

import scala.util.control.NonFatal

/**
  * Created by lad on 14.02.16.
  */
object TicTacToeApp {
  def main(args: Array[String]) {
    Application.launch(classOf[TicTacToeApp], args: _*)
  }
}






class TicTacToeAppController extends Initializable {


  @FXML var topLeftBtn: Button = _
  @FXML var topCenterBtn: Button = _
  @FXML var topRightBtn: Button = _

  @FXML var middleLeftBtn: Button = _
  @FXML var middleCenterBtn: Button = _
  @FXML var middleRightBtn: Button = _

  @FXML var bottomLeftBtn: Button = _
  @FXML var bottomCenterBtn: Button = _
  @FXML var bottomRightBtn: Button = _


  /**
    * has to be a lazy val, otherwise you'll get a nullpointer when accessing the map since
    * the buttons are injected only in the initialize method which is called after the constructor
    *
    */
  lazy val buttons: Map[TMove, Button] =
    Map(
      TopLeft -> topLeftBtn,
      TopCenter -> topCenterBtn,
      TopRight -> topRightBtn,
      MiddleLeft -> middleLeftBtn,
      MiddleCenter -> middleCenterBtn,
      MiddleRight -> middleRightBtn,
      BottomLeft -> bottomLeftBtn,
      BottomCenter -> bottomCenterBtn,
      BottomRight -> bottomRightBtn
    )

  val strategyProperty: SimpleObjectProperty[TicTacToeStrategy] = new SimpleObjectProperty[TicTacToeStrategy](RandomTicTacToeStrategy)

  def getStrategy(): TicTacToeStrategy = strategyProperty.get()

  def setStrategy(s: TicTacToeStrategy) = strategyProperty.set(s)

  val currentGameProperty: SimpleObjectProperty[TicTacToe] = new SimpleObjectProperty(TicTacToe())
  currentGameProperty.addListener(JfxUtils.onChange(updateGame))

  def getCurrentGame(): TicTacToe = currentGameProperty.get()

  def setCurrentGame(t: TicTacToe) = currentGameProperty.set(t)

  @FXML var resultLabel: Label = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {

  }

  def updateGame(oldGame: TicTacToe, curGame: TicTacToe): Unit = {
    if (curGame.isOver) {
      curGame.winner match {
        case None => resultLabel.setText("Draw")
        case Some((p,_)) if p == PlayerA =>  resultLabel.setText("Winner: PlayerA")
        case Some((p,_)) if p == PlayerB =>  resultLabel.setText("Winner: PlayerB")
      }
      buttons.values.foreach(_.setDisable(true))
    }
  }

  @FXML def newGame(): Unit = {
    resultLabel.setText("")
    setCurrentGame(TicTacToe())
    buttons.values.foreach {
      case b =>
        b.setText("")
        b.setDisable(false)
    }
  }

  def turn(strategy: TicTacToeStrategy, player: Player, m: TMove): Unit = {
    updateButtons(m,player)
    val gameAfterTurn: TicTacToe = getCurrentGame().turn(m, player)
    setCurrentGame(gameAfterTurn)

    // computer move
    strategy.calcNextTurn(gameAfterTurn).map({
      case move =>
        updateButtons(move, gameAfterTurn.nextPlayer)
        gameAfterTurn.turn(move, gameAfterTurn.nextPlayer)
    }).foreach(setCurrentGame)
  }

  def updateButtons(m: TMove, player: Player): Unit = {
    buttons(m).setText(player.asString)
    buttons(m).setDisable(true)
  }

  @FXML def onTopLeftClick(): Unit = turn(getStrategy(), getCurrentGame().nextPlayer, TopLeft)

  @FXML def onTopCenterClick(): Unit = turn(getStrategy(), getCurrentGame().nextPlayer, TopCenter)

  @FXML def onTopRightClick(): Unit = turn(getStrategy(), getCurrentGame().nextPlayer, TopRight)


  @FXML def onMiddleLeftClick(): Unit = turn(getStrategy(), getCurrentGame().nextPlayer, MiddleLeft)

  @FXML def onMiddleCenterClick(): Unit = turn(getStrategy(), getCurrentGame().nextPlayer, MiddleCenter)

  @FXML def onMiddleRightClick(): Unit = turn(getStrategy(), getCurrentGame().nextPlayer, MiddleRight)


  @FXML def onBottomLeftClick(): Unit = turn(getStrategy(), getCurrentGame().nextPlayer, BottomLeft)

  @FXML def onBottomCenterClick(): Unit = turn(getStrategy(), getCurrentGame().nextPlayer, BottomCenter)

  @FXML def onBottomRightClick(): Unit = turn(getStrategy(), getCurrentGame().nextPlayer, BottomRight)


}

/**
  * Created by lad on 02.01.16.
  */
class TicTacToeApp extends javafx.application.Application {

  private val resource: URL = getClass.getResource("TicTacToeApp.fxml")
  assert(resource != null)
  val loader = new FXMLLoader(resource)

  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("TicTacToe App")
      loader.load[Parent]()
      stage.setScene(new Scene(loader.getRoot[Parent]))
      stage.show()
    } catch {
      case NonFatal(e) => e.printStackTrace()
    }

}
