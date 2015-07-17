package sample.stream.experiments

import monifu.concurrent.Implicits.globalScheduler
import monifu.reactive.{ ConnectableObservable, Observable }
import monifu.reactive.subjects.PublishSubject

import scala.concurrent.duration.DurationInt

object ABCD3 {
  def main(args: Array[String]) {
    val observable = Observable.interval(1.second)

    val subject = PublishSubject[Long]()

    val connectableObservable = ConnectableObservable(observable, subject)

    connectableObservable.connect

    subject.take(10).foreach(println)

    Thread.sleep(5000)

    subject.take(10).foreach(println)

    Thread.sleep(20000)
  }
}
