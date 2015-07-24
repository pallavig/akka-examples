package sample.stream.experiments

import java.io.File

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity.Chunked
import akka.http.scaladsl.model._
import akka.stream.io.{ SynchronousFileSink, SynchronousFileSource }
import akka.stream.scaladsl.{ Sink, Source }
import akka.util.ByteString

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }

class AService {
  import A._

  def copyFile = {
    println("accepted connection")

    val file = new File("src/main/resources/logfile.txt")
    val source: Source[ByteString, Future[Long]] = SynchronousFileSource(file)

    val synchronousFileSink = SynchronousFileSink(new File("abc.txt"), append = true)
    val graph = source.to(synchronousFileSink)
    graph.run()
  }

  def send = {
    val poolFlow = Http().cachedHostConnectionPool[Int]("localhost", 8081)

    val source = Source.single(HttpRequest(uri = "/") -> 42)

    val runWith = source.via(poolFlow)
      .runWith(Sink.head)

    runWith.map { b: (Try[HttpResponse], Int) =>
      b._1.foreach { x =>
        val source = x.entity.dataBytes
        source.runWith(Sink.foreach(println))
      }
      b._2
    }
  }

  def chunked(): Future[Unit] = {
    val fileSource = SynchronousFileSource(new File("abc.txt"))

    val chunkedDataSource = Chunked.fromData(ContentTypes.`application/octet-stream`, fileSource)

    val poolFlow = Http().cachedHostConnectionPool[Unit]("localhost", 8081)

    val request = HttpRequest(method = HttpMethods.POST, uri = "/chunked", entity = chunkedDataSource)

    val source = Source.single(request -> ())

    source.via(poolFlow).mapAsync(1) {
      case (Success(resp), _) => resp.toStrict(1.second)
      case (Failure(ex), _)   => Future.failed[HttpResponse](ex)
    }.runForeach(println)
  }

  def storeRequestEntity(byteStrings: Source[ByteString, Any]) = {
    byteStrings.runWith(SynchronousFileSink(new File("def.txt"), append = true))
  }

}