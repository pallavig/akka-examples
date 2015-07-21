package sample.stream.experiments

import rx.lang.scala.Observable

object ObservableExample {
  def main(args: Array[String]) {

    //    def print(n: Int) {
    //      println(n)
    //    }
    //
    //    def printThrowable(t: Throwable) {
    //      println(t)
    //    }

    //why is this a partially applied function?
    //    val observer = Observer.apply[Int](print _, printThrowable _)
    //    val observable = Observable.from(List(1, 2, 3))
    //    val subscription = observable.subscribe(observer)
    //    subscription.unsubscribe()
    //    println("subscription 1", subscription.isUnsubscribed)
    //    println("subscription 2", subscription.isUnsubscribed)

    //    val observable1 = Observable.from(Future.failed(new NoSuchElementException))
    //    observable1.subscribe(observer)

    //    val first = Observable.just(10, 11, 12)
    //    val second = Observable.just(10, 11, 12)
    //    val booleans = for ((n1, n2) <- first zip second) yield n1 == n2
    //    println(booleans)

    //    observable.subscribe(observer)

    //    val o = Observable.interval(Duration(1, java.util.concurrent.TimeUnit.SECONDS)).take(5)
    //    o.subscribe(n => println("n = " + n))
    //    Observable.just(1, 2, 3, 4).reduce(_ + _)

    val observable = Observable.from(1 to 10)
    observable.subscribe { x =>
      println(x)
    }
    observable.subscribe { x =>
      println(x)
    }
  }
}
