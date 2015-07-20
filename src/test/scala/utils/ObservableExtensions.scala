package utils

import rx.lang.scala.subjects.PublishSubject
import rx.lang.scala.{ Observable, Observer }

object ObservableExtensions {

  implicit class RichObservable[T](val observable: Observable[T]) extends AnyVal {
    def bulkSubscribe(observers: List[Observer[T]]) = {
      observers.map(x => observable.subscribe(x))
    }

    def multicast = observable.replay

    def hot = {
      val x = PublishSubject[T]()
      observable.take(10).subscribe(x)
      x
    }
  }
}
