package scalatest

import scala.annotation.tailrec

import org.scalatest.{Canceled, Failed, FunSuite, Matchers, Outcome, Retries}
import RetryTest._
import org.scalatest.tagobjects.Retryable

class RetryTest extends FunSuite with Matchers with Retries {

  private val helloFunction = new FunctionThatFailsForFirstNIterations(2)

  private val retries = 5

  override def withFixture(test: NoArgTest): Outcome =
    if (isRetryable(test)) retry(test, retries) else super.withFixture(test)

  @tailrec
  private def retry(test: NoArgTest, count: Int): Outcome = super.withFixture(test) match {
    case Failed(_) | Canceled(_) => if (count == 1) super.withFixture(test) else retry(test, count - 1)
    case outcome => outcome
  }

  test("flaky test", Retryable) {
    helloFunction.helloWorld() should contain("Hello World!")
  }
}

object RetryTest {

  class FunctionThatFailsForFirstNIterations(failureCount: Int) {

    private var iteration: Int = 0

    def helloWorld(): Option[String] = {
      val result = if (iteration < failureCount) None else Some("Hello World!")
      iteration = iteration + 1
      println(s"Iteration: $iteration")
      result
    }
  }
}