package messages

case class Event(eventType: String, data: String, timestamp: Long)

case class Stats()

case class SayHello(name: String)

