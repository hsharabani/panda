package actors

import akka.actor._
import messages.SayHello

object HelloActor {
  def props = Props[HelloActor]

}

class HelloActor extends Actor {
  import HelloActor._

  def receive = {
    case SayHello(name: String) =>
      println ("HelloActor.receive");
      sender() ! "Hello, " + name
  }
}