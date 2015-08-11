package sample.stream.tic_tac_toe.back_end

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpHeader, HttpResponse }
import akka.http.scaladsl.model.headers.{ `Access-Control-Allow-Headers`, `Access-Control-Allow-Origin`, HttpOrigin, HttpOriginRange }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object Configs {
  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
}

object Main {
  def main(args: Array[String]) {
    import Configs._

    val route: Route = {
      val accessControlAllowOrigin: HttpHeader = `Access-Control-Allow-Origin`.apply(HttpOrigin("http://localhost:8080"))
      val accessControlAllowHeaders = `Access-Control-Allow-Headers`(List("Access-Control-Allow-Origin"))

      pathPrefix("tic") {
        path(Rest) { event =>
          println(event)
          complete(HttpResponse(headers = List(accessControlAllowOrigin, accessControlAllowHeaders)))
        }
      }
    }

    Http().bindAndHandle(route, "localhost", 8082)
  }
}
