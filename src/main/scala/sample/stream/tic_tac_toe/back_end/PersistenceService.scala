package sample.stream.tic_tac_toe.back_end

import akka.actor.ActorRef
import akka.persistence.PersistentActor

class PersistenceService(movesTracker: ActorRef) {
  def persist(event: TickEvent) = {
    movesTracker ! event
  }
}

class MovesTracker extends PersistentActor {
  override def receiveRecover: Receive = {
    case event => println(event)
  }

  override def receiveCommand: Receive = {
    case event: TickEvent =>
      persist(event) { event =>
        println("event persisted")
      }
  }

  override def persistenceId: String = "tic-tac-toe"
}
