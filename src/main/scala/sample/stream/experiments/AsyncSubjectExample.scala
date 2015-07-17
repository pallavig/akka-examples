package sample.stream.experiments

import rx.lang.scala.Observable
import rx.lang.scala.subjects.AsyncSubject

import scala.concurrent.duration.DurationInt

object AsyncSubjectExample {
  def main(args: Array[String]) {

    val observable = Observable.interval(1.second)
      .take(10)

    val subject = AsyncSubject[Long]()

    observable.subscribe(subject)

    subject.subscribe(x => println(x))

    Thread.sleep(10000)
  }
}
