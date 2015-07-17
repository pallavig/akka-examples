package sample.stream.experiments

import rx.lang.scala.subjects.ReplaySubject

object ReplaySubjectExample {
  def main(args: Array[String]) {
    val subject = ReplaySubject[String]()
    subject.onNext("one")
    subject.onNext("two")
    subject.onNext("three")
    subject.onCompleted()

    // both of the following will get the onNext/onCompleted calls from above
    subject.subscribe { x: String =>
      println("observer 1")
      println(x)
    }
    subject.subscribe { x: String =>
      println("observer 2")
      println(x)
    }
  }
}
