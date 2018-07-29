package messages

case class Event(eventType: String, data: String, timestamp: Long)

case class StatsRequest()

case class StatsResult(result: String)

case class ParseLine(line: String)
