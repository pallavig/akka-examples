package sample.stream.experiments

import rx.lang.scala.{ Observer, Observable }

import scala.concurrent.duration._

object HotAndCold {
  def main(args: Array[String]) {
    val observable = Observable.interval(1.seconds)

    val observer: Observer[Long] = Observer { x: Long =>
      println("observer 1")
      println(x)
    }

    def a(observer: Observer[Long]) = {
      observable.subscribe(observer)
    }

    val observable1 = Observable.create(a)

    observable.subscribe { x: Long =>
      println("observer 1")
      println(x)
    }

    Thread.sleep(10000)

    observable.subscribe { x: Long =>
      println("observer 2")
      println(x)
    }

    Thread.sleep(10000)
  }
}
