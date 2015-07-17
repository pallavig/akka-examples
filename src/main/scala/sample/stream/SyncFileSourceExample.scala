package sample.stream

import java.io.File
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.io.SynchronousFileSource
import akka.stream.scaladsl.{ Sink, Source }
import akka.util.ByteString
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object SyncFileSourceExample {
  def main(args: Array[String]) {
    implicit val actorSystem = ActorSystem("server")
    implicit val materializer = ActorMaterializer()

    val file = new File("src/main/resources/logfile.txt")
    val source: Source[ByteString, Future[Long]] = SynchronousFileSource(file)
    val foreach: Sink[ByteString, Future[Int]] = Sink.fold(0) { (u, t) =>
      u + 1
    }
    val graph = source.to(foreach)
    val future = graph.run()
    future.onSuccess {
      case a => println("hey", a)
    }
  }
}
