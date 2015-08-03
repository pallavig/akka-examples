package sample.stream.data_transfer.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import sample.stream.data_transfer.http.Configs._
import sample.stream.data_transfer.utils.CommonMarshallers

object Main {
  def main(args: Array[String]) {
    new Servers().main
  }
}

object Configs {
  implicit val actorSystem = ActorSystem("http-server")
  implicit val materializer = ActorMaterializer()
}

class Servers extends CommonMarshallers {
  def main = {
    val service = new ProducingService()

    val sourceRoute: Route = {
      path("send") {
        post {
          onSuccess(service.send) {
            complete("done")
          }
        }
      }
    }

    val receiverRoute: Route = {
      pathPrefix("send") {
        post {
          path(Rest) { filename =>
            entity(as[Source[ByteString, Any]]) { byteStrings =>
              onSuccess(service.store(filename, byteStrings)) { done =>
                complete("done")
              }
            }
          }
        }
      } ~
        path("test") {
          get {
            complete("jinda")
          }
        }
    }

    Http().bindAndHandle(sourceRoute, "localhost", 8080)
    Http().bindAndHandle(receiverRoute, "localhost", 8081)
  }
}
