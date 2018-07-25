package akkastreams

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}

object PrintIntegers extends App {

  implicit val system: ActorSystem = ActorSystem("PrintIntegers")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 100).throttle(1, 100.milliseconds)
  val doubler: Flow[Int, Int, NotUsed] = Flow[Int].map(_ * 2)
  val sink: Sink[Int, Future[Done]] = Sink.foreach[Int](println)
  val runnable: RunnableGraph[Future[Done]] = source.via(doubler).toMat(sink)(Keep.right)
  val run: Future[Done] = runnable.run()
  run.onComplete(_ => system.terminate())
}
