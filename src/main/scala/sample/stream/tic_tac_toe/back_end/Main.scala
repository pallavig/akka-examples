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

case class TickEvent(tickSign: String, coOrdinates: CoOrdinates)

case class CoOrdinates(x: Int, y: Int)

object CoOrdinates {
  def apply(commaSeperatedCoOrdinates: String): CoOrdinates = {
    val points = commaSeperatedCoOrdinates.split(",").map(_.toInt)
    CoOrdinates(points(0), points(1))
  }
}

object Main {

  def parse(event: String) = {
    val inputs = event.split("/")
    val tickSign = inputs(1)
    val coOrdinates = CoOrdinates.apply(inputs(0))
    TickEvent(tickSign, coOrdinates)
  }

  def main(args: Array[String]) {
    import Configs._

    val route: Route = {
      val accessControlAllowOrigin: HttpHeader = `Access-Control-Allow-Origin`.apply(HttpOrigin("http://localhost:8080"))
      val accessControlAllowHeaders = `Access-Control-Allow-Headers`(List("Access-Control-Allow-Origin"))

      pathPrefix("tic") {
        path(Rest) { event =>
          println(parse(event))
          complete(HttpResponse(headers = List(accessControlAllowOrigin, accessControlAllowHeaders)))
        }
      }
    }

    Http().bindAndHandle(route, "localhost", 8082)
  }
}
