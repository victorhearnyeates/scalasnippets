package zio

import scala.concurrent.duration._

import cats.implicits._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}
import zio.internal.Platform
import zio.stream._

class RateLimiterTest extends FunSuite with Matchers with ScalaFutures {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = 60.seconds, interval = 500.millis)

  val runtime = Runtime.unsafeFromLayer(ZEnv.live, Platform.default)
  val rateLimiter: RateLimiter3 = runtime.unsafeRun(RateLimiter3.create)

  test("Test rate limited stuff") {

    def runInt(i: Int): (Long, Int) = {
      println(s"Running: $i")
      Thread.sleep(500)
      println(s"Completed: $i")
      System.currentTimeMillis() -> i
    }

    def rateLimited(highPriority: Boolean)(i: Int): Task[(Long, Int)] = {
      println(s"Calling function with: $i")
      rateLimiter.rateLimit(Task.succeed(runInt(i)), highPriority)
    }

    def isEven(i: Int): Boolean = i % 2 == 0

    val numbers: List[Int] = (1 to 10).toList

    val evenStream: Stream[Throwable, (Long, Int)] =
      Stream.fromIterable(numbers).filter(isEven).mapMPar(3)(rateLimited(true))

    val oddStream: Stream[Throwable, (Long, Int)] =
      Stream.fromIterable(numbers).filter(!isEven(_)).mapMPar(3)(rateLimited(false))

    val task: Task[List[(Long, Int)]] = ZIO
      .collectAllPar(List(oddStream.runCollect, evenStream.runCollect))
      .map(_.flatten.sortBy(_._1))

    val result: List[(Long, Int)] = runtime.unsafeRun(task)
    println(result)
  }
}
