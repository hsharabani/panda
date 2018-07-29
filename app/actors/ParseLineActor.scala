package actors

import akka.actor._
import messages.{Event, ParseLine}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, _}

import scala.util.{Failure, Success, Try}

/**
  * Actor that parses lines.
  */
object ParseLineActor {
  def props: Props = Props[ParseLineActor]
}

class ParseLineActor extends Actor {

  override def receive: Receive = {
    case ParseLine(line) =>
      Try(Json.parse(line)) match {
        case Success(json) =>
          json.validate[Event] match {
            case JsSuccess(event, _) =>
              sender() ! event
            case JsError(errors) => Console.err.println(s"line error: $line $errors")
          }
        case Failure(f) => Console.err.println(s"line error: $line")
      }
  }

  implicit val eventReads: Reads[Event] = (
    (JsPath \ "event_type").read[String] and
      (JsPath \ "data").read[String] and
      (JsPath \ "timestamp").read[Long]
    ) (Event.apply _)
}
