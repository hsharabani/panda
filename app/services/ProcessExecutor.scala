package services

import javax.inject._

import scala.concurrent.{ExecutionContext, Future}
import scala.sys.process._

@Singleton
class ProcessExecutor @Inject()(eventMainService: EventMainService)(implicit exec: ExecutionContext) {

  val result: Stream[String] = Seq(ProcessExecutor.executableLocation).lineStream
  val future: Future[Unit] = Future {
    result.foreach(line => {
      eventMainService.parseLine(line)
    })
  }
}

object ProcessExecutor {
  val executableLocation : String = "./generator-linux-amd64"
}