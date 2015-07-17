package sample.stream.experiments

import rx.lang.scala.Observable

import scala.concurrent.duration._

object MultiConsumedObserver {
  def main(args: Array[String]) {
    //    val observable: Observable[Int] = Observable.from(1 to 10).filter(_ % 2 == 0)
    val observable = Observable.interval(1.second)
      .map(x => { println(x); x })

    println(observable)

    observable.subscribe(x => println("obs1", x))
    println("*" * 50)
    Thread.sleep(5000)
    observable.subscribe(x => println("obs2", x))
    println("*" * 50)

    Thread.sleep(10000)
  }
}
