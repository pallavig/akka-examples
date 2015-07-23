package sample.stream.actors

import akka.actor.{Actor, ActorSystem, Props}

class MyActor2 extends Actor {
  override def receive: Receive = {
    case "test" => println("forwarded message")
  }
}

class MyActor extends Actor {

  val myActor = context.actorOf(Props[MyActor2], name = "myactor")

  override def receive = {
    case x if x == "test" => {
      println("test message", myActor)
      myActor.forward(x)
    }
  }

}

object MyActorMain {
  def main(args: Array[String]) {
    val system = ActorSystem("MySystem")
    val myActor = system.actorOf(Props[MyActor], name = "myactor")
    myActor ! "test"

    Thread.sleep(1000)
    system.shutdown()
  }
}
