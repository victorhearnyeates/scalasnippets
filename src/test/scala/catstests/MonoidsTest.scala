package catstests

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class MonoidsTest extends FunSuite with Matchers with ScalaFutures {

  import cats._
  import cats.implicits._

  test("FoldMap Option") {
    val list: List[String] = List("1", "2", "3")
    def f(s: String): Option[Int] = Try(s.toInt).toOption
    val result: Option[Int] = Traverse[List].foldMap(list)(f)
    result.get should === (6)
  }

  test("FoldMap Future") {

    implicit val booleanMonoid: Monoid[Boolean] = new Monoid[Boolean] {
      val empty: Boolean = true
      def combine(x: Boolean, y: Boolean): Boolean = x && y
    }

    val list1: List[Future[Boolean]] = List(Future.successful(true), Future.successful(false))
    val list2: List[Future[Boolean]] = List(Future.successful(true), Future.successful(true))

    val result1: Future[Boolean] = Traverse[List].fold(list1)
    val result2: Future[Boolean] = Traverse[List].fold(list2)

    result1.futureValue should === (false)
    result2.futureValue should === (true)
  }
}
