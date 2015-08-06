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
  def play(co_ordinates: (Int, Int), sign: String): Board = {
    val boxes = grid(co_ordinates._2).updated(co_ordinates._1, Box(Some(sign)))
    Board(grid.updated(co_ordinates._2, boxes))
  }
}

case class Game(players: (Player, Player), board: Board) {
  def currentPlayer = players._1

  def next = Game((players._2, players._1), board)

  def play(player: Player, co_ordinates: (Int, Int)) = {
    val newGame = Game(players, board.play(co_ordinates, player.sign))
    println(newGame.board.grid)
    newGame
  }
}

object UiListener extends JSApp {

  def appendButtonWithId(id: String) = {
    val button11 = document.createElement("button")
    button11.textContent = "  "
    button11.id = id
    document.body.appendChild(button11)
  }

  def clickEventStream(button: Button): Observable[MouseEvent] = {
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
    val buttonIds = List("0-0", "0-1", "0-2", "1-0", "1-1", "1-2", "2-0", "2-1", "2-2")
    val buttons = buttonIds.map(appendButtonWithId).map(_.asInstanceOf[Button])
    buttons.map(button => clickEventStream(button).subscribe { event: MouseEvent => buttonClicked(button) })

    def buttonClicked(element: Button) = {
      val currentPlayer = game.currentPlayer
      element.textContent = currentPlayer.sign
      game = game.next
      val clickedButtonsIdentity = element.id.split("-")
      game = game.play(currentPlayer, (clickedButtonsIdentity(0).toInt, clickedButtonsIdentity(1).toInt))
      Cancel
    }

  }
}
