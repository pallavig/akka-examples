package sample.stream.data_transfer.http

import java.io.File

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity.Chunked
import akka.http.scaladsl.model._
import akka.stream.io.{ SynchronousFileSink, SynchronousFileSource }
import akka.stream.scaladsl.{ FlattenStrategy, Sink, Source }
import akka.util.ByteString
import sample.stream.data_transfer.utils.UrlExtensions

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

class ProducingService {
  import Configs._
  def store(filename: String, byteStrings: Source[ByteString, Any]) = {
    println(s"writing to $filename")
    byteStrings.runWith(SynchronousFileSink(new File(outputDir + filename), append = true))
  }

  val inputDir = "/usr/local/data/tmt/frames/input/"
  val outputDir = "/usr/local/data/tmt/frames/output/"

  def files = {
    val files = new File(inputDir).listFiles().toList
    Source(files)
  }

  def createRequest(file: File) = {
    val fileSource = SynchronousFileSource(file)
    val chunkedDataSource = Chunked.fromData(ContentTypes.NoContentType, fileSource)
    val uri = UrlExtensions.absoluteUrl(s"send/${file.getName}")
    val request = HttpRequest(method = HttpMethods.POST, uri = uri, entity = chunkedDataSource)
    request -> Uri(uri)
  }

  def send = {
    val poolFlow = Http().superPool[Uri]()

    files
      .map(createRequest)
      .via(poolFlow)
      .mapAsync(1) {
        case (Success(resp), _) => resp.toStrict(1.second)
        case (Failure(ex), _)   => Future.failed[HttpResponse](ex)
      }.runWith(Sink.ignore)
  }
}
