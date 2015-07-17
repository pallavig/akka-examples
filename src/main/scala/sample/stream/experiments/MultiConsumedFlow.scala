package sample.stream.experiments

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.duration.DurationInt

object MultiConsumedFlow {
  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem("server")
    implicit val materializer = ActorMaterializer()
    import actorSystem.dispatcher

    val source = Source(1.second, 1.second, 1)
      .scan(0)(_ + _)
      .take(10)
      .map(x => { println(x); x })

    val sink = Sink.foreach[Int](x => println("ob1", x))
    val sink2 = Sink.foreach[Int](x => println("ob1", x))

    val runnableGraph = FlowGraph.closed(sink, sink2)(Keep.both) { implicit builder =>
      (s, s2) =>
        import FlowGraph.Implicits._

        val bcast = builder.add(Broadcast[Int](2))

        source ~> bcast.in
        bcast.out(0) ~> Flow[Int].map(_ + 10) ~> s
        bcast.out(1) ~> Flow[Int].map(_ + 20) ~> s2
    }

    val (f1, f2) = runnableGraph.run()

    //    val f1 = source.runForeach(x => println("obs1", x))
    //    val f2 = source.runForeach(x => println("obs2", x))
    //
    f1.flatMap(_ => f2).onComplete(_ => actorSystem.shutdown())

  }
}
