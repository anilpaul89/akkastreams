package me.anil.akka.graph

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}

object ParallelGraphApp extends App {

  val firstSink = Sink.foreach(println)
  val secondSink = Sink.foreach(println)

  val sharedDoubler = Flow[Int].map(_ * 2)

  implicit val actorSystem = ActorSystem("GraphDSL")
  implicit val materializer = ActorMaterializer()

  val graph = RunnableGraph.fromGraph(GraphDSL.create(firstSink, secondSink)((_, _)) { implicit builder =>
    (topHS, bottomHS) =>
      import GraphDSL.Implicits._
      val broadcast = builder.add(Broadcast[Int](2))
      Source.single(1) ~> broadcast.in

      broadcast ~> sharedDoubler ~> topHS.in
      broadcast ~> sharedDoubler ~> bottomHS.in
      ClosedShape
  })
  graph.run
  actorSystem.terminate()
}
