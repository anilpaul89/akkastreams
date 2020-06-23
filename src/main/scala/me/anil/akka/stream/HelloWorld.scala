package me.anil.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

object HelloWorld extends App {

  implicit val actorSystem = ActorSystem("HelloWorld")
  implicit val actorMaterializer = ActorMaterializer()

  val source = Source("Hello world example")

  // If we put println each char will be printed in new line
  val sink = Sink.foreach(print)

  val graph = source.to(sink)
  graph.run()
  actorSystem.terminate()
}
