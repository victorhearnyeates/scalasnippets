package zio

import scala.concurrent.Future
import scala.concurrent.duration._

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, KillSwitches, SharedKillSwitch}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}
import zio.TaskUtils._
import zio.ZIOAbortTest._
import zio.clock.Clock

class ZIOAbortTest extends FunSuite with Matchers with ScalaFutures {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = 20.seconds, interval = 200.millis)
  private val runtime = new DefaultRuntime {}

  test("Running a program with abort") {

    val program = new MyProgram(5)

    val abortConfig = new AbortConfig {
      val repeatDuration: Duration = 2.seconds
      def abort(): Unit = program.abort()
      def doAbort(): Boolean = program.doAbort()
    }

    val task: ZIO[Clock, Throwable, Int] = taskWithAbort(program.task, abortConfig)
    val result: Int = runtime.unsafeRun(task)
    result should be > 4
  }

  test("Running an Akka stream with abort") {

    implicit val system: ActorSystem = ActorSystem("SumIntegers")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val program = new MyAkkaStreamProgram(5)

    val abortConfig = new AbortConfig {
      val repeatDuration: Duration = 2.seconds
      def abort(): Unit = program.abort()
      def doAbort(): Boolean = program.doAbort()
    }

    val task: ZIO[Clock, Throwable, Int] = taskWithAbort(program.task, abortConfig)
    val result: Int = runtime.unsafeRun(task)
    result should be > 4
  }
}

object TaskUtils {

  trait AbortConfig {
    val repeatDuration: Duration
    def abort(): Unit
    def doAbort(): Boolean
  }

  def taskWithAbort[A](task: Task[A], config: AbortConfig): ZIO[Clock, Throwable, A] = {
    def error: Throwable = new RuntimeException("This should never happen!")
    task.raceEither(abortTask(config)) flatMap {
      case Left(a) => Task.succeed(a)
      case Right(_) => Task.fail(error)
    }
  }

  private def abortTask(config: AbortConfig): URIO[Clock, Unit] = {
    val retry = zio.duration.Duration.fromScala(config.repeatDuration)
    val schedule = Schedule.spaced(retry)
    def run: Unit = if (config.doAbort()) config.abort() else ()
    UIO(run).repeat(schedule).map(_ => ())
  }
}

object ZIOAbortTest {

  class MyProgram(abortIteration: Int) {

    private var aborted: Boolean = false
    private var iteration: Int = 0

    def doAbort(): Boolean = {
      val r = iteration > abortIteration
      println(s"doAbort = $r")
      r
    }

    def abort(): Unit = {
      println("ABORTED")
      aborted = true
    }

    val task: Task[Int] = Task {
      while(!aborted) {
        iteration = iteration + 1
        println(iteration)
        Thread.sleep(1000)
      }
      iteration
    }
  }

  class MyAkkaStreamProgram(abortIteration: Int)(implicit m: ActorMaterializer) {

    private var iteration: Int = 0
    private val killSwitch: SharedKillSwitch = KillSwitches.shared("my-kill-switch")
    private val source: Source[Int, NotUsed] = Source(1 to 10).throttle(1, 1.second)

    private val doubler: Flow[Int, Int, NotUsed] = Flow[Int].map { i =>
      iteration = iteration + 1
      println(i)
      i * 2
    }

    private val sink: Sink[Int, Future[Int]] = Sink.fold(0)(_ + _)

    private val runnable: RunnableGraph[Future[Int]] = source
      .via(doubler)
      .via(killSwitch.flow)
      .toMat(sink)(Keep.right)

    def doAbort(): Boolean = {
      val r = iteration > abortIteration
      println(s"doAbort = $r")
      r
    }

    def abort(): Unit = {
      println("ABORTED")
      killSwitch.shutdown()
    }

    val task: Task[Int] = Task.fromFuture(_ => runnable.run())
  }
}