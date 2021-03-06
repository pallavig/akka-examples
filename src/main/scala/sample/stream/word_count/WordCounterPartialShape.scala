package sample.stream.word_count

import java.io.File

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.io.SynchronousFileSource
import akka.stream.scaladsl._
import akka.util.ByteString

object WordCounterPartialShape {
  implicit val actorSystem = ActorSystem("word-counter")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]) {
    val fileSource = SynchronousFileSource(new File("abc.txt"))

    import actorSystem.dispatcher
    import FlowGraph.Implicits._

    val flow = Flow[ByteString].map(_.utf8String)

    val graph: Graph[FlowShape[ByteString, Int], Unit] = FlowGraph.partial() { implicit builder =>

      val broadcast = builder.add(Broadcast[ByteString](1))

      val ops1 = broadcast.out(0) ~> flow.map(_.length)

      FlowShape(broadcast.in, ops1.outlet)
    }

    val customFlow = Flow.wrap(graph)
    val eventualLong = fileSource.viaMat(customFlow)(Keep.left).to(Sink.foreach(println)).run()
    eventualLong.onComplete(_ => actorSystem.shutdown())
  }

  def characterCounterFlow = {
    Flow() { implicit builder =>
      val flowShape = builder.add(Flow[String].map(_.length))
      (flowShape.inlet, flowShape.outlet)
    }
  }

}
