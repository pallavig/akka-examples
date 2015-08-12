package sample.stream.tic_tac_toe.back_end

import akka.actor.{ Props, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpHeader, HttpResponse }
import akka.http.scaladsl.model.headers.{ `Access-Control-Allow-Headers`, `Access-Control-Allow-Origin`, HttpOrigin, HttpOriginRange }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import scala.concurrent.duration.DurationInt

object Configs {
  implicit val actorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val timeout = Timeout(5 seconds)
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

  import Configs._
  private val persistenceService = new PersistenceService(actorSystem.actorOf(Props(new MovesTracker)))

  def parse(event: String) = {
    val inputs = event.split("/")
    val tickSign = inputs(1)
    val coOrdinates = CoOrdinates.apply(inputs(0))
    TickEvent(tickSign, coOrdinates)
  }

  def main(args: Array[String]) {
    import Configs._

    val movesTrackerRoute: Route = {
      val accessControlAllowOrigin: HttpHeader = `Access-Control-Allow-Origin`.apply(HttpOrigin("http://localhost:8080"))
      val accessControlAllowHeaders = `Access-Control-Allow-Headers`(List("Access-Control-Allow-Origin"))

      pathPrefix("tic") {
        path(Rest) { event =>
          persistenceService.persist(parse(event))
          complete(HttpResponse(headers = List(accessControlAllowOrigin, accessControlAllowHeaders)))
        }
      }
    }

    val gameRoute: Route = {
      pathSingleSlash {
        getFromResource("web/index.html")
      } ~
        path("akka-stream-scala-fastopt.js") {
          getFromFile("./target/scala-2.11/akka-stream-scala-fastopt.js")
        }
    }

    Http().bindAndHandle(movesTrackerRoute, "localhost", 8082)
    Http().bindAndHandle(gameRoute, "localhost", 8080)
  }
}
