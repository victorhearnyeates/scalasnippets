package zio

import java.util.UUID
import scala.concurrent.Future
import scala.concurrent.duration._

import org.scalatest.{FunSuite, Matchers}

class ZIORetryTest extends FunSuite with Matchers {

  import ZIORetryTest._

  val runtime = new DefaultRuntime {}

  test("stuff") {

    def run: Unit = {
      val id = UUID.randomUUID().toString
      println(s"Running task #$id")
      Thread.sleep(10000)
      println(s"Finished #$id")
    }

    def task: Task[Unit] = Task.fromFuture{ implicit ec => Future(run) }
    runtime.unsafeRun(retryTaskWithTimeout(task, 2.seconds, 3))
  }
}

object ZIORetryTest {

  def retryTaskWithTimeout[A](
    task: Task[A],
    timeout: Duration,
    retryCount: Int
  ) = {
    def timeoutError: Throwable = new RuntimeException("Task timed out")
    val retrySchedule = Schedule.spaced(zio.duration.Duration.fromScala(1.second)).whileOutput(_ < retryCount - 1)
    val timeoutDuration = zio.duration.Duration.fromScala(timeout)
    val taskWithTimeout = task.timeoutFail(timeoutError)(timeoutDuration)
    val x = taskWithTimeout.mapError{ e => println(e.getMessage); e }
    x.retry(retrySchedule)
  }
}