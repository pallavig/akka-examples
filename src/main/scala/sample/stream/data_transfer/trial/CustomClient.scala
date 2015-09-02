package sample.stream.data_transfer.trial

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.scaladsl.Sink

object CustomClient {
  import sample.stream.data_transfer.http.Configs._
  def main(args: Array[String]) {
    val eventualResponse = Http().singleRequest(HttpRequest(uri = "http://localhost:8081/dummy"))

    val dataBytes = eventualResponse.map { response =>
      response.entity.dataBytes
    }

    dataBytes.onSuccess {
      case source =>
        source.runWith(Sink.foreach(println))
    }
  }
}
