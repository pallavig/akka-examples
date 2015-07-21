package sample.stream.tic_tac_toe

import monifu.concurrent.Implicits.globalScheduler
import monifu.reactive.Ack.Cancel
import monifu.reactive.Observable
import monifu.reactive.subjects.PublishSubject
import org.scalajs.dom._
import org.scalajs.dom.html.Button
import org.scalajs.dom.raw.MouseEvent

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

case class Player(id: Int, sign: String) {}

case class Box(sign: Option[String] = None)

case class Board(grid: List[List[Box]]) {
//  def play(co_ordinates: (Int, Int), sign: String): Board = {
//
//  }
}

case class Game(players: (Player, Player), board: Board) {
  def currentPlayer = players._1

  def next = Game((players._2, players._1), board)

//  def play(player: Player, co_ordinates: (Int, Int)) = {
//    Game(players, board.play(co_ordinates, player.sign))
//  }
}

object UiListener extends JSApp {

  def appendButtonWithId(id: String) = {
    val button11 = document.createElement("button")
    button11.textContent = "  "
    button11.id = id
    document.body.appendChild(button11)
  }

  def mouseEventStream(button: Button): Observable[MouseEvent] = {
    val subject = PublishSubject[MouseEvent]()
    button.onclick = { event: MouseEvent =>
      subject.onNext(event)
    }
    subject
  }

  def init = {
    Game((Player(1, "X"), Player(2, "O")), Board(List(
      List(Box(), Box(), Box()),
      List(Box(), Box(), Box()),
      List(Box(), Box(), Box()))
    ))
  }

  @JSExport
  override def main(): Unit = {

    var game = init

    val buttonIds = List("button11", "button12", "button13", "button21", "button22", "button23", "button31", "button32", "button33")
    val buttons = buttonIds.map(appendButtonWithId).map(_.asInstanceOf[Button])

    buttons.map(button => mouseEventStream(button).subscribe { event: MouseEvent => buttonClicked(button) })

    def buttonClicked(element: Button) = {
      element.textContent = game.currentPlayer.sign
      game = game.next
//      Game(game.players, )
      Cancel
    }
  }
}
