package sample.stream.word_count

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.io.SynchronousFileSource
import akka.stream.scaladsl._
import akka.util.ByteString

object FileReaderSourceShape {

  implicit val actorSystem = ActorSystem("FileReaderSourceShape")
  implicit val materialiser = ActorMaterializer()
  import actorSystem.dispatcher

  def main(args: Array[String]): Unit = {
    val fileSource = utf8FileReader("abc.txt")

    val eventualUnit = fileSource.runWith(Sink.foreach(println))
    eventualUnit.onComplete(_ => actorSystem.shutdown())
  }

  def utf8FileReader(filename: String) = {
    Source() { implicit builder =>
      import FlowGraph.Implicits._
      val source = builder.add(SynchronousFileSource(new File(filename)))

      val utf8converterFlow = builder.add(Flow[ByteString].map(_.utf8String))
      source ~> utf8converterFlow.inlet

      utf8converterFlow.outlet
    }
  }
}
