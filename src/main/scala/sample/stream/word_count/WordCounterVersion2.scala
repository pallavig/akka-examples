package sample.stream.word_count

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

object WordCounterVersion2 {
  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem("WordCounterVersion2")
    implicit val actorMaterializer = ActorMaterializer()
    import actorSystem.dispatcher

    val fileSource = FileReaderSourceShape.utf8FileReader("abc.txt")
    val wordCounter = fileSource.via(WordCounterPartialShape.characterCounterFlow)

    wordCounter.runWith(Sink.foreach(println)).onComplete(_ => actorSystem.shutdown())

  }
}
