package futureretry

import akka.actor.ActorSystem
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._

class FutureRetryTest extends FunSuite with Matchers with ScalaFutures {

  test("Generate exponentially increasing intervals with base 2") {
    FutureRetry.exponentialIntervals(1.second, base = 2).take(7).toList should be (
      List(1, 2, 4, 8, 16, 32, 64).map(_.seconds)
    )
  }

  test("Retry function until it succeeds") {
    implicit val s = ActorSystem("retryTest").scheduler
    def failWhenLessThanFour(n: Int): Future[Int \/ Int] = Future.successful(if (n < 4) n.left else n.right)
    def increment(n1: Int, n2: Int): Int = n1 + 1
    val retryIntervals: List[FiniteDuration] = List.fill(10)(1.millisecond)
    FutureRetry.retry(failWhenLessThanFour, retryIntervals, increment)(1).futureValue should be (4)
  }
}
