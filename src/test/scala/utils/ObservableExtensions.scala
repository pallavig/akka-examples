package utils

import rx.lang.scala.{ Observable, Observer }

object ObservableExtensions {

  implicit class RichObservable[T](val observable: Observable[T]) extends AnyVal {
    def bulkSubscribe(observers: List[Observer[T]]) = {
      observers.map(x => observable.subscribe(x))
    }

    def multicast = observable.replay
  }
}
