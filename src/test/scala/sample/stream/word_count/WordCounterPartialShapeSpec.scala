package sample.stream.word_count

import akka.stream.scaladsl.{ Sink, Source }
import akka.testkit.TestProbe
import org.scalatest.FunSpec

class WordCounterPartialShapeSpec extends FunSpec {
  describe("character counter") {
    it("should count character") {
      import WordCounterPartialShape.actorSystem
      import WordCounterPartialShape.materializer

      val probe = TestProbe()
      val source = Source.single("hi, I am here")
      val characterCounterFlow = WordCounterPartialShape.characterCounterFlow

      source.via(characterCounterFlow).to(Sink.actorRef(probe.ref, "completed")).run()

      probe.expectMsg(13)
      ()
    }
  }

}
