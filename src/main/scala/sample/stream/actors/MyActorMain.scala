package sample.stream.actors

import akka.actor.{Props, ActorSystem, Actor}

class MyActor extends Actor {
  val myActor = context.actorOf(Props[MyActor], name = "myactor")

  override def receive = {
    case "test" => println("test message", myActor)
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
