package net.ladstatt.fx.ttt

import java.net.URL
import java.util.ResourceBundle
import javafx.application.Application
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control.{Button, TextArea}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import scala.util.control.NonFatal


object TicTacToeApp {
  def main(args: Array[String]) {
    Application.launch(classOf[TicTacToeApp], args: _*)
  }
}


object JfxUtils {

  /**
    * This function serves as syntactic sugar for creating a changelistener by providing a
    * suitable function.
    *
    * @param onChangeAction the action to be performed when a change happened.
    * @tparam T type which is observed.
    * @return
    */
  def mkChangeListener[T](onChangeAction: (ObservableValue[_ <: T], T, T) => Unit): ChangeListener[T] = {
    new ChangeListener[T]() {
      override def changed(observable: ObservableValue[_ <: T], oldValue: T, newValue: T) = {
        onChangeAction(observable, oldValue, newValue)
      }
    }
  }

  def relisten[T](observableValue: ObservableValue[_ <: T], oldT: T, newT: T)(f: (T, T) => Unit): Unit = {
    f(oldT, newT)
  }

  /**
    * Most often you are only interested in the new value, sometimes also in the old
    * value. This function makes it easier to create changelistener which exactly do this
    * by providing a suitable function as parameter.
    *
    * @param f function to be executed when a change happenes
    * @tparam T the underlying type which is observed
    * @return
    */
  def onChange[T](f: (T, T) => Unit): ChangeListener[T] = {
    mkChangeListener[T](relisten(_, _, _)(f))
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

  val strategyProperty: SimpleObjectProperty[TicTacToeStrategy] = new SimpleObjectProperty[TicTacToeStrategy](BruteForceTicTacToeStrategy)

  def getStrategy(): TicTacToeStrategy = strategyProperty.get()

  def setStrategy(s: TicTacToeStrategy) = strategyProperty.set(s)

  val currentGameProperty: SimpleObjectProperty[TicTacToe] = new SimpleObjectProperty(TicTacToe())
  currentGameProperty.addListener(JfxUtils.onChange(updateGame))

  def getCurrentGame(): TicTacToe = currentGameProperty.get()

  def setCurrentGame(t: TicTacToe) = currentGameProperty.set(t)

  @FXML var logTextView: TextArea = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {

  }

  def updateGame(oldGame: TicTacToe, curGame: TicTacToe): Unit = {
    if (curGame.isOver) {
      logTextView.appendText(curGame.asString)
    }
  }

  @FXML def newGame(): Unit = {
    logTextView.clear()
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
