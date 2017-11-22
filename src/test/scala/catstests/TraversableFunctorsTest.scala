package catstests

import cats.data.State
import cats.Traverse
import cats.implicits._

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}

import language.higherKinds
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TraversableFunctorsTest extends FunSuite with Matchers with ScalaFutures {

  test("traverse") {

    val traverse: Future[List[Int]] =
      Traverse[List].traverse[Future, Int, Int](List(1, 2))(Future.successful)

    traverse.futureValue should === (List(1, 2))

    val flatTraverse: Future[Option[Int]] =
      Traverse[Option].flatTraverse[Future, Int, Int](Option(99))(i => Future.successful(Option(i)))

    flatTraverse.futureValue should === (Option(99))
  }

  test("traverse List with State") {

    def traverseS[F[_]: Traverse, S, A, B](as: F[A])(f: A => State[S, B]): State[S, F[B]] = {
      type f[x] = State[S, x]
      as.traverse[f, B](f)
    }

    def accumulate(n: Int): State[Int, Unit] = State.modify(_ + n)

    traverseS(List(1, 2, 3))(accumulate).runS(10).value should === (16)
  }
}
