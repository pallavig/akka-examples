package sample.stream.experiments

import monifu.reactive.Ack.Continue
import monifu.reactive.subjects.PublishSubject
import monifu.concurrent.Implicits.globalScheduler

import org.scalajs.dom.document
import org.scalajs.dom.html.Button
import org.scalajs.dom.raw.MouseEvent

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object TutorialApp extends JSApp {
  def main(args: Array[String]) {
    main()
  }

  @JSExport
  override def main(): Unit = {
    val element = document.createElement("button")
    element.textContent = "Magic"
    element.id = "magic-button"
    document.body.appendChild(element)

    val magicButton = document.getElementById("magic-button").asInstanceOf[Button]

    val subject = PublishSubject[MouseEvent]()

    subject.subscribe { x =>
      println(x)
      Continue
    }

    magicButton.onclick = { clickEvent: MouseEvent =>
      subject.onNext(clickEvent)
    }
  }
}
