package sample.stream.rx_observables

import org.scalatest.FunSpec
import rx.lang.scala.Observable
import rx.lang.scala.subjects.PublishSubject
import sample.stream.rx_observables.TExtensions.RichT
import utils.ObservableExtensions.RichObservable
import utils.Observables

import scala.concurrent.duration.DurationInt

class PushTest extends FunSpec with AA {

  describe("hot") {
    it("hot-multicast") {
      fork(Observables.numberStream.hot)
      Thread.sleep(5000)
    }
  }

  describe("cold") {

    it("cold-unicast") {
      fork(Observables.numberStream.onBackpressureBlock(3))
      Thread.sleep(5000)
    }

    it("cold-multicast") {
      val numberStream = Observables.numberStream.publish

      numberStream.take(10).subscribe(doublingSubject)
      numberStream.connect
      Thread.sleep(500)
      numberStream.take(10).subscribe(squaringSubject)

      Thread.sleep(20000)
    }

    it("multicast-single-trigger") {
      val numberStream = Observables.numberStream.publish

      numberStream.take(10).subscribe(doublingSubject)
      numberStream.take(10).subscribe(squaringSubject)
      numberStream.connect

      Thread.sleep(20000)
    }
  }

  val doublingSubject = PublishSubject[Long]()
  doublingSubject.delay(200.millis).map(doubling).map(doubled).take(10).foreach(ignore)
  val squaringSubject = PublishSubject[Long]()
  squaringSubject.map(squaring).map(squared).take(10).foreach(ignore)

  def fork(xs: Observable[Long]) = {
    xs.subscribe(doublingSubject)
    Thread.sleep(500)
    separator()
    xs.subscribe(squaringSubject)
  }
}

object TExtensions {
  implicit class RichT[T](val x: T) extends AnyVal {
    def log(prefix: String = "") = {
      println(s"$prefix: $x")
      x
    }
  }
}

trait AA {
  def squaring(x: Long) = { println(s"--- squaring:$x"); x * x }
  def doubling(x: Long) = { println(s"*** doubling:$x"); x * 2 }

  def initialized(x: Long) = x.log("^^^ initialized")
  def squared(x: Long) = x.log("+++ squared value")
  def doubled(x: Long) = x.log("!!! doubled value")

  def separator() = println("=" * 50)
  def ignore(x: Long) = ()
}
