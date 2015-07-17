package sample.stream.experiments

import rx.Scheduler
import rx.lang.scala.{ Scheduler, Observable }

import scala.concurrent.duration._

object ReplayBuffer {
  def main(args: Array[String]) {
    //replays the last two things
    val observable = Observable.interval(1.second)

    val connectableObservable = observable.replay(bufferSize = 3)

    connectableObservable.subscribe(x => { println("one"); println(x) })
    connectableObservable.connect

    Thread.sleep(10000)

    connectableObservable.subscribe(x => { println("two"); println(x) })

    Thread.sleep(10000)

    //    Scheduler
    //
    //    observable.replay(scheduler = Scheduler())

    observable.share

  }
}
