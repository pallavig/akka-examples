package sample.stream.data_transfer.trial

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse, StatusCode }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object CustomFlow {
  import sample.stream.data_transfer.http.Configs._

  lazy val eventualResponse = Http().singleRequest(HttpRequest(uri = "http://localhost:8080/dummy"))

  def main(args: Array[String]): Unit = {
    val route: Route = {
      path("dummy") {
        onSuccess(eventualResponse) { response =>
          complete(HttpResponse(entity = response.entity, status = StatusCode.int2StatusCode(200)))
        }
      }
    }
    Http().bindAndHandle(route, "localhost", 8081)
  }
}