package futureretry

import scalaz._
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import akka.pattern.after
import akka.actor.Scheduler
import scala.util.control.NonFatal

/** FutureRetry is a module with functions to run and retry computations that generate a Future that may fail and
  * the result inside the future can either be success or failure.
  */
object FutureRetry {

  /** Run and retry a function that generates a Future.
    * The result inside the future can either be a right (success) or left (failure) disjunction.
    * It will retry if the Future fails or the result inside the future is a left disjunction.
    * The input value can be modified on each retry as specified by the modify function.
    *
    * @param op The result of this function is a Future that contains a result that is either a
    *           success (right disjuntion) or failure (left disjunction).
    * @param retryIntervals A list of time intervals to retry failures.
    * @param modify A function that takes the current input value and the result value, and computes
    *               a new input value for the next retry.
    * @param a The input value.
    */
  def retry[A, B](
    op: A => Future[B \/ B],
    retryIntervals: Seq[FiniteDuration],
    modify: (A, B) => A
  )(a: A)(implicit ec: ExecutionContext, s: Scheduler): Future[B] = {

    def delayThenRetry(d: FiniteDuration, ds: Seq[FiniteDuration], modified: A): Future[B] =
      after(d, s)(retry(op, ds, modify)(modified))

    def handleError(d: FiniteDuration, ds: Seq[FiniteDuration]): PartialFunction[Throwable, Future[B]] = {
      case NonFatal(_) => delayThenRetry(d, ds, a)
    }

    def processResult(d: FiniteDuration, ds: Seq[FiniteDuration])(result: B \/ B): Future[B] = result match {
      case \/-(success) => Future.successful(success)
      case -\/(failure) => delayThenRetry(d, ds, modify(a, failure))
    }

    val resultF: Future[B \/ B] = op(a)

    retryIntervals match {
      case Nil => resultF.map(_.merge)
      case d :: ds => resultF.flatMap(processResult(d, ds)).recoverWith(handleError(d, ds))
    }
  }

  /** Run and retry a function that generates a Future.
    * The result inside the future can either be a right (success) or left (failure) disjunction.
    * It will retry if the Future fails or the result inside the future is a left disjunction.
    *
    * @param op The result of this function is a Future that contains a result that is either a
    *           success (right disjuntion) or failure (left disjunction).
    * @param retryIntervals A list of time intervals to retry failures.
    * @param a The input value.
    */
  def retry[A, B](
    op: A => Future[B \/ B],
    retryIntervals: Seq[FiniteDuration]
  )(a: A)(implicit ec: ExecutionContext, s: Scheduler): Future[B] = {
    def noModification(a: A, b: B): A = a
    retry(op, retryIntervals, noModification)(a)
  }

  def exponentialIntervals(unitDelay: FiniteDuration, base: Int): Stream[FiniteDuration] =
    exponentialSeries(base).map(_ * unitDelay)

  private def exponentialSeries(base: Int, exponent: Int = 0): Stream[Int] =
    Math.pow(base, exponent).toInt #:: exponentialSeries(base, exponent + 1)
}
