package controllers

import javax.inject._
import play.api.mvc._
import services.EventMainService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class EventController @Inject()(eventMainService: EventMainService,
                                cc: ControllerComponents)
  extends AbstractController(cc) {

  def stats(): Action[AnyContent] = Action.async {
    eventMainService.getStats.map { message =>
      Ok(message)
    }
  }

}