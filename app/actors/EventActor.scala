package actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import messages._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._


object EventActor {
  def props: Props = Props[EventActor]
}

class EventActor extends Actor {
  implicit val timeout: Timeout = 5.seconds

  private val parserActor = context.actorOf(Props[ParseLineActor], "parser")
  private val counterActors: List[ActorRef] = List(
    context.actorOf(Props[EventTypeCounterActor], "event-type-counter"),
    context.actorOf(Props[WordCounterActor], "event-word-counter"))

  def receive(): Receive = {
    case p@ParseLine(_) =>
      parserActor ! p

    case e@Event(_, _, _) =>
      counterActors.foreach(_ ! e)

    case s@StatsRequest() =>
      val mySender = sender()
      val eventualStrings: Future[List[String]] = Future.sequence(counterActors.map(actor => (actor ? s).mapTo[StatsResult].map(_.result)))
      eventualStrings.map(_.mkString("\n"))
        .map(str => mySender ! StatsResult(str))
  }
}
