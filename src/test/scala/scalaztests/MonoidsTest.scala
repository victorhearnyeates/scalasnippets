package scalaztests

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class MonoidsTest extends FunSuite with Matchers with ScalaFutures {

  import scalaz._
  import Scalaz._

  test("FoldMap Option") {
    val list: List[String] = List("1", "2", "3")
    def f(s: String): Option[Int] = Try(s.toInt).toOption
    val result: Option[Int] = Traverse[List].foldMap(list)(f)
    result.get should === (6)
  }

  test("FoldMap Future") {

    implicit val booleanMonoid: Monoid[Boolean] = Monoid.instance(_ && _, true)

    implicit def futureMonoid[A](implicit M: Monoid[A]): Monoid[Future[A]] = {
      def append(f1: Future[A], f2: => Future[A]): Future[A] =
        Applicative[Future].apply2(f1, f2)((a1, a2) => M.append(a1, a2))
      Monoid.instance(append, Future.successful(M.zero))
    }

    val list1: List[Future[Boolean]] = List(Future.successful(true), Future.successful(false))
    val list2: List[Future[Boolean]] = List(Future.successful(true), Future.successful(true))

    val result1: Future[Boolean] = Traverse[List].suml(list1)
    val result2: Future[Boolean] = Traverse[List].suml(list2)

    result1.futureValue should === (false)
    result2.futureValue should === (true)
  }
}
