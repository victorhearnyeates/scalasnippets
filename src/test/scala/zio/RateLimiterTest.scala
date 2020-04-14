package zio

import scala.annotation.tailrec
import scala.concurrent.duration._

import cats.implicits._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}
import zio.clock.Clock
import zio.duration.Duration
import zio.stream._

class RateLimiterTest extends FunSuite with Matchers with ScalaFutures {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = 60.seconds, interval = 500.millis)

  val runtime = Runtime.default

  test("Test RateLimiter1") {

    val duration = 100.milliseconds

    val rateLimiter: RateLimiter = runtime.unsafeRun(RateLimiter.create)

    def run(i: Int): ZIO[Clock, Nothing, (Int, Long)] = for {
      _ <- ZIO.sleep(Duration.fromScala(duration))
      end <- zio.clock.nanoTime
    } yield (i, end)

    def check(results: List[(Int, Long)]): Boolean = {

      val timeDiffs: List[(Int, Long, Long)] =
        compareConsecutiveElements[(Int, Long), (Int, Long, Long)](results.sortBy(_._2).reverse) {
          case ((i1, t1), (_, t2)) => (i1, t1, (t1 - t2))
        }.sortBy(_._2)

      timeDiffs.forall{ case (_, _, diff) => diff > duration.toNanos }
    }

    def rateLimited(i: Int): ZIO[Clock, Nothing, (Int, Long)] =
      rateLimiter.rateLimit(run(i))

    val task = ZIO.foreachPar((1 to 10).toList)(rateLimited).map(check)
    runtime.unsafeRun(task)
  }

  test("Test RateLimiter3") {

    val rateLimiter: RateLimiter3 = runtime.unsafeRun(RateLimiter3.create)

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
    println(normaliseTime(result))
  }

  def compareConsecutiveElements[A, B](as: List[A])(f: (A, A) => B): List[B] = {
    @tailrec
    def run(remaining: List[A], acc: List[B]): List[B] = remaining match {
      case a1 :: a2 :: t => run(a2 :: t, acc :+ f(a1, a2))
      case _ => acc
    }
    run(as, Nil)
  }

  def normaliseTime(r: List[(Long, Int)]): List[(Long, Int)] = {

    @tailrec
    def run(
      remaining: List[(Long, Int)],
      prev: Option[(Long, Int)],
      acc: List[(Long, Int)]
    ): List[(Long, Int)] = (remaining, prev) match {
      case (Nil, _) => acc
      case ((t, i) :: tail, None) => run(tail, Some((t, i)), acc :+ (0, i))
      case ((t2, i2) :: tail, Some((t1, _))) => run(tail, Some((t2, i2)), acc :+ (t2 - t1, i2))
    }

    run(r, None, Nil)
  }
}
