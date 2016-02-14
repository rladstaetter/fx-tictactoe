package net.ladstatt.tictactoe

import javafx.beans.value.{ChangeListener, ObservableValue}

/**
  * Created by lad on 14.02.16.
  */
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
