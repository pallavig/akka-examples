package sample.stream.rx_observables

import java.util.stream.Collectors

import collection.JavaConverters._
import org.scalatest.{ FunSpec, MustMatchers }
import sample.stream.rx_observables.TExtensions.RichT

class RsSuite extends FunSpec with MustMatchers {
  def numberStream = (1 to 10).toStream

  def list = numberStream.asJava

  def javaNumberStream = list.stream().mapToInt(identity(_)).map(initialized(_))

  def squaring(x: Int) = {
    println(s"--- squaring:$x"); x * x
  }

  def doubling(x: Int) = {
    println(s"*** doubling:$x"); x * 2
  }

  def initialized(x: Int) = x.log("^^^ initialized")

  def squared(x: Int) = x.log("+++ squared value")

  def doubled(x: Int) = x.log("!!! doubled value")

  def separator() = println("=" * 50)

  def ignore(x: Int) = ()
}

class PullTest extends RsSuite {
  describe("lazy") {
    describe("reusable") {
      it("cached") {
        initializeAndFork(numberStream)
      }
    }
    describe("perishable") {
      it("perishable-simple") {
        separator()
        val numberStream = javaNumberStream

        numberStream.map(squaring(_)).map(squared(_)).forEach(ignore(_))
        separator()
        numberStream.map(doubling(_)).map(doubled(_)).forEach(ignore(_))
      }
      it("perishable-cached") {
        val numberStream = javaNumberStream.toArray.toList.asJava
        separator()
        numberStream.stream().mapToInt(x => identity(x)).map(squaring(_)).map(squared(_)).forEach(ignore(_))
        separator()
        numberStream.stream().mapToInt(x => identity(x)).map(doubling(_)).map(doubled(_)).forEach(ignore(_))
        separator()
      }
    }

  }

  def initializeAndFork(numberStream: Stream[Int]) = {
    separator()
    val ys = numberStream.map(initialized)
    forkSeq(ys)
  }

  def forkSeq(xs: Seq[Int]) = {
    separator()
    xs.map(squaring).map(squared).foreach(ignore)
    separator()
    xs.map(doubling).map(doubled).foreach(ignore)
    separator()
  }
}
