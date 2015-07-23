package sample.stream.akka_http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Sink, Source }

object ConnectionLevelExample {
  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem("akka-connection-level")
    implicit val materialiser = ActorMaterializer()
    implicit val executionContextExecutor = actorSystem.dispatcher

    val outgoingConnection = Http().outgoingConnection("akka.io")

    val eventualResponse = Source.single(HttpRequest(uri = "/"))
      .via(outgoingConnection)
      .runWith(Sink.head)

    eventualResponse.map({ x => println(x); actorSystem.shutdown(); 1 })
  }

}
