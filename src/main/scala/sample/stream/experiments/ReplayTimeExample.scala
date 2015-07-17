package sample.stream.experiments

import java.util.concurrent.TimeUnit
import rx.lang.scala.Observable
import scala.concurrent.duration.{ DurationInt, Duration }

object ReplayTimeExample {
  def main(args: Array[String]) {
    val observable = Observable.interval(1.second)

    val connectableObservable1 = observable.replay(Duration(2L, TimeUnit.SECONDS))
    connectableObservable1.subscribe(x => { println("one"); println(x) })
    connectableObservable1.connect

    Thread.sleep(5000)

    connectableObservable1.subscribe(x => { println("two"); println(x) })

    Thread.sleep(5000)
  }
}
