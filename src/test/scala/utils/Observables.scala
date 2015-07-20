package utils

import rx.lang.scala.{ Observable, Observer }

import scala.concurrent.duration.DurationInt

object Observables {

  def log(x: Long) = {
    println(s"initialized $x")
    x
  }

  def numberStream: Observable[Long] = Observable.interval(1.seconds).drop(1).take(10).map(log)

}

object Observers {
  def doubling = {
    Observer { x: Long =>
      println(s"doubling $x")
      //      Thread.sleep(1000)
      println(s"doubled ${x * 2}")
    }
  }

  def squaring = {
    Observer { x: Long =>
      println(s"squaring $x")
      //      Thread.sleep(2000)
      println(s"squared ${x * x}")
    }
  }
}
