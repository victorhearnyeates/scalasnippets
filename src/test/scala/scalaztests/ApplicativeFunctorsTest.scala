package scalaztests

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ApplicativeFunctorsTest extends FunSuite with Matchers with ScalaFutures {

  import scalaz._
  import Scalaz._

  test("Functor Composition") {

    val f1: Future[Option[Int]] = Future.successful(Option(2))
    val fo = Functor[Future].compose[Option]
    val result: Future[Option[Int]] = fo.map(f1)(_ * 3)
    result.futureValue.get should === (6)
  }

  test("Applicative Composition") {

    val f1: Future[Option[Int]] = Future.successful(Option(2))
    val f2: Future[Option[Int]] = Future.successful(Option(3))
    val fo = Applicative[Future].compose[Option]
    val result: Future[Option[Int]] = fo.apply2(f1, f2)(_ + _)
    result.futureValue.get should === (5)
  }
}
