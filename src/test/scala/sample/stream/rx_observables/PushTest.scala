package sample.stream.rx_observables

import org.scalatest.FunSpec
import utils.ObservableExtensions.RichObservable
import utils.{ Observables, Observers }

class PushTest extends FunSpec {
  
  describe("cold") {
    it("multicast") {
      println("cold multicast")

      val numberStream = Observables.numberStream.multicast
      Thread.sleep(2000)
      val list = List(Observers.doubling, Observers.squaring)
      numberStream.bulkSubscribe(list)
      numberStream.connect
      Thread.sleep(21000)
      assert(1 == 1)
    }
    it("unicast") {
      println("cold multicast")

      val numberStream = Observables.numberStream
      numberStream.subscribe(Observers.doubling)
      Thread.sleep(2000)
      numberStream.subscribe(Observers.squaring)
      Thread.sleep(21000)
      assert(1 == 1)
    }
  }
}
