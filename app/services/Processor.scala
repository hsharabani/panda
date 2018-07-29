package services

import controllers.EventController
import javax.inject._
import messages._
import play.api.inject.ApplicationLifecycle
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.concurrent.Future
import scala.sys.process._

@Singleton
class Processor @Inject()(appLifecycle: ApplicationLifecycle, eventController: EventController) {

  println("here")
  val result: Stream[String] = Seq("/home/owner/Downloads/generator-linux-amd64").lineStream
  result.foreach(line => {
    println(s"line: $line")
    val json = Json.parse(line)
    json.validate[Event] match {
      case JsSuccess(event, _) =>
        eventController.event(event)
      case _ => Console.err.println(s"error: $line")
    }
  })

  appLifecycle.addStopHook { () =>
    Future.successful(())
  }

  implicit val eventReads: Reads[Event] = (
    (JsPath \ "eventType").read[String] and
      (JsPath \ "data").read[String] and
      (JsPath \ "timestamp").read[Long]
    ) (Event.apply _)
}
