package actors

import akka.actor._
import messages._

object EventTypeCounterActor {
  def props: Props = Props[EventTypeCounterActor]
}

class EventTypeCounterActor extends Actor {

  override def preStart(): Unit = {
    println("EventTypeCounterActor started")
  }

  def receive(typeCount: Map[String, Long]): Receive = {
    case Event(eventType, _, _) =>
      val updatedCounts: Map[String, Long] = typeCount.updated(eventType, typeCount.getOrElse(eventType, 0L) + 1)
      context become receive(updatedCounts)
    case Stats() =>
      println(s"EventTypeCounterActor.Stats: ${"Words: " + typeCount.toString()}")
      sender() ! ("Words: " + typeCount.map{case (k,v) => s"$k => $v"}.mkString(","))
  }

  override def receive: Receive = receive(Map.empty)
}
