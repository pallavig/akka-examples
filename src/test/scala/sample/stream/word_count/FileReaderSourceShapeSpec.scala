package sample.stream.word_count

import akka.stream.scaladsl.Sink
import akka.testkit.TestProbe
import org.scalatest.{ FunSpec, MustMatchers }

class FileReaderSourceShapeSpec extends FunSpec with MustMatchers {
  describe("utf-8 file source") {
    it("should read file") {
      import FileReaderSourceShape.{ actorSystem, materialiser }

      val filename = "/Users/pallavig/projects/self/akka-stream-scala/src/test/resources/test-file"
      val source = FileReaderSourceShape.utf8FileReader(filename)
      val probe = TestProbe()

      source.to(Sink.actorRef(probe.ref, "completed")).run()

      probe.expectMsgAnyClassOf("".getClass) //find right way to do this
      ()
    }
  }
}
