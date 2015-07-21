package sample.stream.experiments

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

final case class Author(handle: String)

final case class Hashtag(name: String)

final case class Tweet(author: Author, timestamp: Long, body: String) {
  def hashtags: Set[Hashtag] =
    body.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
}

object ReactiveTweets {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("reactive-tweets")
    implicit val materializer = ActorMaterializer()
    //
    //    val akka = Hashtag("#akka")
    //    val tweets: Source[Tweet, Unit] = Source.apply(List(Tweet(Author("abc"), 1234, "#akka")))
    //
    //    val authors: Source[Author, Unit] =
    //      tweets
    //        .filter(_.hashtags.contains(akka))
    //        .map(_.author)
    //
    //    authors.runWith(Sink.foreach(println))
    //
    //    val hashtags: Source[Hashtag, Unit] = tweets.mapConcat(_.hashtags.toList)

    //does not work
    //    val source = Source(1 to 10)
    //    source.map(_ => 0) // has no effect on source, since it's immutable
    //    source.runWith(Sink.fold(0)(_ + _)) // 55
    //
    //    val zeroes = source.map(_ => 0) // returns new Source[Int], with `map()` appended
    //    zeroes.runWith(Sink.fold(0)(_ + _)) // 0

    //    val source: Source[Int, Unit] = Source(1 to 10)
    //    val sink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)
    //
    //    // connect the Source to the Sink, obtaining a RunnableFlow
    //    source.to(sink)
    //    val runnable: RunnableGraph[Unit] = source.to(sink)
    //
    //    // materialize the flow
    //    val materialized = runnable.run()
    //
    //    // get the materialized value of the FoldSink
    //    val sum: Future[Int] = materialized.get(sink)

    //    val source = Source(1 to 10)
    //    val sink = Sink.fold[Int, Int](0)(_ + _)
    //
    //    // materialize the flow, getting the Sinks materialized value
    //    val sum: Future[Int] = source.runWith(sink)
    //
    //    sum.onSuccess {
    //      case s => println(s)
    //    }

    val source = Source(1 to 10)
    source.map(_ => 0) // has no effect on source, since it's immutable
    //    println("*************")
    //    println(source.shape.outlets)
    //    val loggedSource = source.map { elem =>
    //      println("***********", elem)
    //      elem
    //    }
    //    loggedSource.log("before-map")
    //      .withAttributes(Attributes.logLevels(onElement = Logging.WarningLevel))

    //    val runWith = source.runWith(Sink.fold(0)(_ + _))
    //    runWith.onSuccess {
    //      case s => println(s)
    //    } // 55
    //
    //        val zeroes = source.map(_ => 0) // returns new Source[Int], with `map()` appended
    //        val runWith1 = zeroes.runWith(Sink.fold(0)(_ + _))
    //        runWith1.onSuccess {
    //          case s => println(s)
    //        }
    //
    //
    //        val map: Flow[Int, String, Unit] = Flow[Int].map { a =>
    //          a.toString
    //        }
    //
    ////    Flow[Input].map(_.toIn)
    //    Sink

    //    Source(1 to 3)
    //      .map { i => println(s"A: $i"); i }
    //      .map { i => println(s"B: $i"); i }
    //      .map { i => println(s"C: $i"); i }
    //      .runWith(Sink.ignore)

    import scala.concurrent.duration._
    case class Tick()

    FlowGraph.closed() { implicit b =>
      import FlowGraph.Implicits._

      val zipper = b.add(ZipWith[Tick, Int, Int]((tick, count) => count))

      Source(initialDelay = 3.second, interval = 3.second, Tick()) ~> zipper.in0

      Source(initialDelay = 1.second, interval = 1.second, "message!")
        .conflate(seed = (_) => 1)((count, _) => count + 1) ~> zipper.in1

      zipper.out ~> Sink.foreach(println)
    }
  }
}
