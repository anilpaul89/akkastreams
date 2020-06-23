package me.anil.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, RunnableGraph, Sink, Source}
import akka.{Done, NotUsed}

import scala.concurrent.Future

object HelloWorldWithAnnotation extends App {

  implicit val actorSystem = ActorSystem("HelloWorld")
  implicit val materializer = ActorMaterializer()

  val source: Source[String,NotUsed] = Source.single("Hello World")
  val sink: Sink[String,Future[Done]] = Sink.foreach(print)
  val flow: Flow[String,String,NotUsed] = Flow[String].map(str => str.toUpperCase())

  val graph: RunnableGraph[NotUsed] = source.via(flow).to(sink)
  graph.run()
  actorSystem.terminate()
}
