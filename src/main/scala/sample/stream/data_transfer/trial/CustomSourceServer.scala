package sample.stream.data_transfer.trial

import java.io.File
import java.nio.file.Files
import sample.stream.data_transfer.http.Configs
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity.Chunked
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.ByteString
import sample.stream.data_transfer.http.ProducingService

import scala.concurrent.Future

object CustomSourceServer {
  val inputDir = "/usr/local/data/tmt/frames/input/"

  private val producingService = new ProducingService
  def sendBytes = producingService.files.take(2).mapAsync(1)(readFile).map(ByteString.apply)
  private def readFile(file: File) = Future(Files.readAllBytes(file.toPath))(Configs.ec)

  val route: Route = {
    path("dummy") {
      val images = sendBytes
      val chunkedDataSource = Chunked.fromData(ContentTypes.NoContentType, images)
      complete(HttpResponse(entity = chunkedDataSource, status = StatusCode.int2StatusCode(200)))
    }
  }

  def main(args: Array[String]) {
    import Configs.actorSystem
    import Configs.materializer
    import Configs.ec

    Http().bindAndHandle(route, "localhost", 8080)
  }
}
