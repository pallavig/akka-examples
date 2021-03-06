package sample.stream.word_count

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.io.SynchronousFileSource
import akka.stream.scaladsl._
import akka.util.ByteString

object WordCounterClosedShape {
  def main(args: Array[String]) {
    val fileSource = SynchronousFileSource(new File("abc.txt"))

    implicit val actorSystem = ActorSystem("word-counter")
    implicit val materializer = ActorMaterializer()
    import actorSystem.dispatcher

    val sink = Flow[Int].map({ x => println(x); x }).toMat(Sink.fold(0)(_ + _))(Keep.right)

    val flow = Flow[ByteString].map(_.utf8String)

    val runnableGraph = FlowGraph.closed(sink, sink, sink)((m1, m2, m3) => m1.flatMap(_ => m2).flatMap(_ => m3).onComplete(xx => { println("hi"); actorSystem.shutdown; })) { implicit builder =>
      (s1, s2, s3) =>
        import FlowGraph.Implicits._

        val broadcast = builder.add(Broadcast[ByteString](3))
        fileSource ~> broadcast.in

        broadcast.out(0) ~> flow.map(_.length) ~> s1
        broadcast.out(1) ~> flow.map(_.split(" ").length) ~> s2
        broadcast.out(2) ~> flow.map(_.split("\n").length) ~> s3
    }
    runnableGraph.run()
  }
}
