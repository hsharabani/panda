# Infinite Stream reader into statics

This application uses Akka and Play frameworks.


Run this using [sbt](http://www.scala-sbt.org/).  If you downloaded this project from <http://www.playframework.com/download> then you'll find a prepackaged version of sbt in the project directory:

1. copy the generator binary into the source code root.

2. sbt runProd

And then go to <http://localhost:9000/stats> to get the tracked statistics.


There are several demonstration files available in this template.

## Services

- ProcessExecutor.scala

  Executor for the external process.

- EventMainService.scala

  Service to acess the EventActor.


## Controllers

- EventController.scala:

  handles  HTTP stats requests.

## Components

- Module.scala:

  Shows how to use Guice to bind all the components needed by your application.

## Akka Actors
- EventActor.scala:

  The main actor in the application.

-  ParseLineActor.scala

   Parses lines into Events and send it to the main EventActor actor.

-  EventTypeCounterActor.scala

   Keeps track and exposes the count per event type.

-  WordCounterActor.scala

   Keeps track  and exposes the count per word.


## Improvement Ideas:

1. Get the executable binary name from configuration like scala.config.
2. Remove Await.result() from EventActor to wait for each counter results.
3. Persist the statics into a database for the case that it will crash.
