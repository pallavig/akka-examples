package sample.stream.experiments

import rx.lang.scala.subjects.BehaviorSubject

object BehaviourSubjectExample {
  def main(args: Array[String]) {

    println("subject1")
    val subject1 = BehaviorSubject[String]("default")
    subject1.subscribe { x: String =>
      println(x)
    }
    subject1.onNext("one")
    subject1.onNext("two")
    subject1.onNext("three")

    println("subject2")
    // observer will receive the "one", "two" and "three" events, but not "zero"
    val subject2 = BehaviorSubject[String]("default")
    subject2.onNext("zero")
    subject2.onNext("one")
    subject2.subscribe { x: String =>
      println(x)
    }
    subject2.onNext("two")
    subject2.onNext("three")

    // observer will receive only onCompleted
    println("subject3")
    val subject3 = BehaviorSubject[String]("default")
    subject3.onNext("zero")
    subject3.onNext("one")
    subject3.onCompleted()
    subject3
      .subscribe(
        onNext = { x: String => println(x) },
        onError = { e: Throwable => println(e) },
        onCompleted = { () => println("complete") }
      )

    //observer will receive only onError
    val subject = BehaviorSubject[String]("default")
    subject.onNext("zero")
    subject.onNext("one")
    subject.onError(new RuntimeException("error"))
    subject
      .subscribe(
        onNext = { x: String => println(x) },
        onError = { e: Throwable => println(e) },
        onCompleted = { () => println("complete") }
      )
  }
}
