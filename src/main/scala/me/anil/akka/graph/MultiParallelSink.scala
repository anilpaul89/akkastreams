package me.anil.akka.graph

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}

import scala.collection.immutable

object MultiParallelSink extends App{

  implicit val actorSystem = ActorSystem("GraphDSL")
  implicit val materializer = ActorMaterializer()

  val sinks = immutable
    .Seq("a", "b", "c")
    .map(prefix => Flow[String].filter(str => str.startsWith(prefix)).to(Sink.foreach(println)))

  val g = RunnableGraph.fromGraph(GraphDSL.create(sinks) {
    implicit b => sinkList =>
      import GraphDSL.Implicits._
      val broadcast = b.add(Broadcast[String](sinkList.size))

      Source(List("ax", "bx", "cx")) ~> broadcast
      sinkList.foreach(sink => broadcast ~> sink)

      ClosedShape
  })

  g.run()
  actorSystem.terminate()

}
