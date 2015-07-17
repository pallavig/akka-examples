package sample.stream

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString

object A {
  implicit val actorSystem = ActorSystem("server")
  implicit val materializer = ActorMaterializer()
  implicit val bytesUnmarshaller = Unmarshaller.strict { entity: HttpEntity =>
    entity.dataBytes
  }
}

object AkkaServerExample {
  import A._

  def main(args: Array[String]) {

    val route1: Route = {
      pathSingleSlash {
        onSuccess(new AService().copyFile) { a: Long =>
          complete("hurray")
        }
      } ~
        path("images" / "objects") {
          getFromResource("web/index.html")
        } ~
        path("akka-stream-scala-fastopt.js") {
          getFromFile("./target/scala-2.11/akka-stream-scala-fastopt.js")
        } ~
        path("send") {
          onSuccess(new AService().send) { x: Int =>
            println(x)
            complete("cool")
          }
        } ~
        path("chunked") {
          post {
            onComplete(new AService().chunked()) { a =>
              complete("sent")
            }
          }
        }
    }

    val route2: Route = {
      pathSingleSlash {
        println("hi")
        complete("done")
      }

      path("chunked") {
        post {
          entity(as[Source[ByteString, Any]]) { byteStrings =>
            onSuccess(new AService().storeRequestEntity(byteStrings)) { a =>
              println("mummy", a)
              complete("hurray")
            }
          }
        }
      }
    }

    println("refresh")

    Http().bindAndHandle(route1, "localhost", 8080)
    Http().bindAndHandle(route2, "localhost", 8081)

    //    val bind = Http().bind("localhost", 8080)
    //
    //    val map = Flow[HttpRequest].map { request =>
    //      HttpResponse(StatusCode.int2StatusCode(200))
    //    }
    //
    //    val sink = Sink.foreach { connection: IncomingConnection =>
    //      println(s"Accepted new connection from: ${connection.remoteAddress}")
    //      connection.handleWith(map)
    //    }
    //
    //    val runnableGraph = bind.to(sink)
    //    runnableGraph.run()
  }

}
