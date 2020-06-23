package me.anil.akka.graph

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge,
  RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}

object GraphApp extends App {

  implicit val actorSystem = ActorSystem("GraphDSL")
  implicit val materializer = ActorMaterializer()
  val graph = RunnableGraph.fromGraph(GraphDSL.create() {
    implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      val source = Source(1 to 10)
      val sink = Sink.foreach(println)
      val bcast = builder.add(Broadcast[Int](3))
      val merge = builder.add(Merge[Int](3))
      val f1, f2, f4 = Flow[Int].map(_ + 1)
      val f3 =  Flow[Int].map(_ + 2)
      val f5 = Flow[Int].filter(x => x % 2 ==0)
      source ~> f1 ~> bcast ~> f2 ~> merge ~> f4  ~> f5 ~> sink
      bcast ~> f3 ~> merge
      bcast ~> f5 ~> merge
      ClosedShape
  })
  graph.run()
  actorSystem.terminate()

}
