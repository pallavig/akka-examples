package sample.stream.experiments

import rx.lang.scala.Observable

import scala.concurrent.duration._

object AmbExample {
  def main(args: Array[String]): Unit = {

    val observable1 = Observable.from(1 to 5)

    val observable2 = observable1.map(_ + 10).delay(1.seconds)

    val observable3 = observable1.map(_ + 20)

    val observable = observable2.amb(observable3)

    observable.subscribe { x: Int =>
      println(x)
    }

    Thread.sleep(10000)

  }
}
