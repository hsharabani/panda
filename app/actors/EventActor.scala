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

  val childActors: List[ActorRef] = List(
    context.actorOf(Props[EventTypeCounterActor], "event-type-counter"),
    context.actorOf(Props[WordCounterActor], "event-word-counter"))

  override def preStart(): Unit = {
    println("EventActor started")
  }

  def receive(): Receive = {
    case e@Event(_, _, _) =>
      childActors.foreach(_ ! e)
    case s@Stats() =>
      val senderActor = sender()
      println(s"Sender: $senderActor s:$s ")
      val eventualStrings: Future[List[String]] = Future.sequence(childActors.map(actor => (actor ? s).mapTo[String]))
      val str: String = Await.result(eventualStrings, 5 second).mkString("\n")
      sender ! str

    //      eventualString.map(res => {
    //       println(s"Sender: ${senderActor} res:$res ")
    //      sender ! str
    //   })
    //Future.sequence(childActors.map(actor => (actor ? s).mapTo[String]))
    //  .map(_.mkString("\n"))
    //  .map(sender() ! _)
  }
}
