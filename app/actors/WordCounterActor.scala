package actors

import akka.actor._
import messages._

/**
  * Actor that counts words.
  */
object WordCounterActor {
  def props: Props = Props[WordCounterActor]

}

class WordCounterActor extends Actor {

  def receive(wordCount: Map[String, Long]): Receive = {
    case Event(_, data, _) =>
      val updatedCounts: Map[String, Long] = data.split(" ").foldLeft(wordCount)((map, word) => map.updated(word, wordCount.getOrElse(word, 0L) + 1L))
      context become receive(updatedCounts)

    case StatsRequest() =>
      sender() ! StatsResult("Words: " + wordCount.map { case (k, v) => s""""$k" --> $v""" }.mkString(", "))
  }

  override def receive: Receive = receive(Map.empty)
}
