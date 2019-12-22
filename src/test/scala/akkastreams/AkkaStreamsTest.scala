package akkastreams

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, KillSwitches, SharedKillSwitch}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}

class AkkaStreamsTest extends FunSuite with Matchers with ScalaFutures {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = 20.seconds, interval = 200.millis)

  implicit val system: ActorSystem = ActorSystem("PrintIntegers")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val sharedKillSwitch: SharedKillSwitch = KillSwitches.shared("my-kill-switch")
  val source: Source[Int, NotUsed] = Source(1 to 5).throttle(1, 2.second)
  val doubler: Flow[Int, Int, NotUsed] = Flow[Int].map{ i => println(i); i * 2 }
  val sink: Sink[Int, Future[Int]] = Sink.fold(0)(_ + _)

  val runnable: RunnableGraph[Future[Int]] = source
    .via(doubler)
    .via(sharedKillSwitch.flow)
    .toMat(sink)(Keep.right)

  test("stuff") {

    val running: Future[Int] = runnable.run()
    running.onComplete(_ => system.terminate())
    // sharedKillSwitch.shutdown() // abort(new RuntimeException("ABORT ABORT !!"))
    running.futureValue shouldEqual 30
  }
}
