package sample.stream.experiments

import rx.lang.scala.subjects.PublishSubject

object PublishSubjectExample {
  def main(args: Array[String]) {
    val subject = PublishSubject[String]()

    // observer1 will receive all onNext and onCompleted events
    subject.subscribe { x: String =>
      println("observer 1")
      println(x)
    }

    subject.onNext("one")
    subject.onNext("two")

    // observer2 will only receive "three" and onCompleted
    subject.subscribe { x: String =>
      println("observer 2")
      println(x)
    }
    subject.onNext("three")
    subject.onCompleted()

  }
}
