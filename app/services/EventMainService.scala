package services

import actors.EventActor
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import javax.inject.{Inject, Singleton}
import messages.{ParseLine, StatsRequest, StatsResult}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class EventMainService @Inject()(system: ActorSystem)(implicit exec: ExecutionContext) {

  private val eventActor = system.actorOf(EventActor.props, "event-actor")
  implicit val timeout: Timeout = 5.seconds

  def parseLine(line: String): Unit = {
    eventActor ! ParseLine(line)
  }

  def getStats: Future[String] = {
    (eventActor ? StatsRequest()).mapTo[StatsResult].map(_.result)
  }
}
