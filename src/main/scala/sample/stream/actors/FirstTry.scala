package sample.stream.actors

import akka.actor.{ ActorSystem, ActorRef, Props, Actor }
import scala.concurrent.duration._
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

case class ProcessStringMsg(string: String)
case class StringProcessedMsg(words: Integer)

class StringCounterActor extends Actor {
  def receive = {
    case ProcessStringMsg(string) => {
      val wordsInLine = string.split(" ").length
      sender ! StringProcessedMsg(wordsInLine)
    }
    case _ => println("Error: message not recognized")
  }
}

case class StartProcessFileMsg()

class WordCounterActor(filename: String) extends Actor {

  private var running = false
  private var totalLines = 0
  private var linesProcessed = 0
  private var result = 0
  private var fileSender: Option[ActorRef] = None

  def receive = {
    case StartProcessFileMsg() => {
      if (running) {
        println("Warning: duplicate start message received")
      } else {
        running = true
        fileSender = Some(sender) // save reference to process invoker
        import scala.io.Source._
        fromFile(filename).getLines.foreach { line =>
          context.actorOf(Props[StringCounterActor]) ! ProcessStringMsg(line)
          totalLines += 1
        }
      }
    }
    case StringProcessedMsg(words) => {
      result += words
      linesProcessed += 1
      if (linesProcessed == totalLines) {
        fileSender.map(_ ! result) // provide result to process invoker
      }
    }
    case _ => println("message not recognized!")
  }
}

object FirstTry {
  def main(args: Array[String]) {
    val system = ActorSystem("System")
    val actor = system.actorOf(Props(new WordCounterActor(args(0))))
    import system.dispatcher
    implicit val timeout = Timeout(25 seconds)

    val future = actor ? StartProcessFileMsg()
    future.map { result =>
      println("Total number of words " + result)
      system.shutdown
    }
  }
}
