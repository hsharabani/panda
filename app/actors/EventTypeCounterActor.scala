package actors

import akka.actor._
import messages._

/**
  * Actor that counts event types.
  */
object EventTypeCounterActor {
  def props: Props = Props[EventTypeCounterActor]
}

class EventTypeCounterActor extends Actor {

  def receive(typeCount: Map[String, Long]): Receive = {
    case Event(eventType, _, _) =>
      val updatedCounts: Map[String, Long] = typeCount.updated(eventType, typeCount.getOrElse(eventType, 0L) + 1)
      context become receive(updatedCounts)
    case StatsRequest() =>
      sender() ! StatsResult("Event Types: " + typeCount.map{case (k,v) => s""""$k" --> $v"""}.mkString(", "))
  }

  override def receive: Receive = receive(Map.empty)
}
