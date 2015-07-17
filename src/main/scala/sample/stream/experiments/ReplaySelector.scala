package sample.stream.experiments

import rx.lang.scala.Observable

import scala.concurrent.duration._

object ReplaySelector {
  def main(args: Array[String]) {
    val observable = Observable.interval(1.second)

    val connectable = observable.replay(selector = (observable: Observable[Long]) => {
      observable.filter(_ % 2 == 0)
    })
    connectable.subscribe { x: Long =>
      println(x)
    }

    Thread.sleep(10000)
  }
}
