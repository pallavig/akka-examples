package sample.stream.experiments

import java.util.concurrent.TimeUnit

import rx.lang.scala.Observable

import scala.concurrent.duration._

object ConnectableObserverExample {
  def main(args: Array[String]) {
    val observable = Observable.interval(1.second)

    val replay = observable.replay
    //      replay.subscribe(x => println("obs1", x))
    //      Thread.sleep(2000)
    //      replay.connect
    //      replay.subscribe(x => println("obs2", x))
    //
    //      Thread.sleep(10000)

    val share = replay.share

  }
}
