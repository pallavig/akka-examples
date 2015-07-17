package sample.stream.experiments

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import monifu.concurrent.Implicits.globalScheduler
import monifu.reactive.{ ConnectableObservable, Observable }
import monifu.reactive.subjects.PublishSubject

import scala.concurrent.duration.DurationInt

object ABCD {
  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem("server")
    implicit val materializer = ActorMaterializer()

    val observable = Observable.interval(1.second)

    val subject = PublishSubject[Long]()

    //    subject.onNext()

    val connectableObservable = ConnectableObservable(observable, subject)

    Thread.sleep(5000)

    connectableObservable.connect

    connectableObservable.foreach(println)
  }
}
