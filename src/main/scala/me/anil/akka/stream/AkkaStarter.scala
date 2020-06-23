package me.anil.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object AkkaStarter extends App{

  implicit val actorSystem = ActorSystem("Starter")
  implicit val materializer = ActorMaterializer()
  val source = Source(1 to 10)
  val sink = Sink.foreach(println)
  val flow = Flow[Int].map(num => num +1)
  val runnableGraph = source.via(flow).to(sink)
  runnableGraph.run()
  actorSystem.terminate()
}
