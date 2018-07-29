package controllers

import actors.EventActor
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import javax.inject._
import messages._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

@Singleton
class EventController @Inject()(system: ActorSystem,
                                cc: ControllerComponents)
  extends AbstractController(cc) {


  private val eventActor = system.actorOf(EventActor.props, "event-actor")

  implicit val timeout: Timeout = 5.seconds

  def stats(): Action[AnyContent] = Action.async {
    (eventActor ? Stats()).mapTo[String].map { message =>
      Ok(message)
    }
  }

  def event(event: Event): Unit = {
    eventActor ! event
  }
}