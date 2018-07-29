package controllers

import play.api.mvc._
import akka.actor._
import javax.inject._

import scala.concurrent.duration._
import akka.pattern.ask
import actors.HelloActor
import akka.util.Timeout
import messages.SayHello

import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class HelloController @Inject()(system: ActorSystem,
                            cc: ControllerComponents)
  extends AbstractController(cc) {

  val helloActor = system.actorOf(HelloActor.props, "hello-actor")

  implicit val timeout: Timeout = 5.seconds

  def sayHello(name: String) : Action[AnyContent] = Action.async {
    (helloActor ? SayHello(name)).mapTo[String].map { message =>
      Ok(message)
    }
  }
}