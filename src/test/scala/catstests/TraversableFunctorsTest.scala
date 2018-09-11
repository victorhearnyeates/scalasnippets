package catstests

import cats.data.State
import cats._
import cats.implicits._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}
import language.higherKinds
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TraversableFunctorsTest extends FunSuite with Matchers with ScalaFutures {

  def sequentialTraverse[F[_], G[_], A, B](as: F[A])(f: A => G[B])(
    implicit FT: Traverse[F], FAP: Applicative[F], GM: Monad[G], FBMonoid: Monoid[F[B]]
  ): G[F[B]] = as.foldLeft[G[F[B]]](GM.pure[F[B]](FBMonoid.empty)) {
    case (acc, nxt) => acc.flatMap[F[B]](bs => f(nxt).map(b => bs |+| b.pure[F]))
  }

  def sequentialTraverseList[G[_] : Monad, A, B](as: List[A])(f: A => G[B]): G[List[B]] =
    as.foldLeft[G[List[B]]](Monad[G].pure(List.empty[B])) {
      case (acc, nxt) => acc.flatMap[List[B]](bs => f(nxt).map(b => bs :+ b))
    }

  def mapTraverse[F[_], G[_], A, B](fas: G[F[A]])(f: A => G[B])(implicit FT: Traverse[F], GM: Monad[G]): G[F[B]] =
    fas.flatMap(_.traverse(f))

  def mapTraverseSequential[F[_], G[_], A, B](fas: G[F[A]])(f: A => G[B])(
    implicit FT: Traverse[F], FAP: Applicative[F], GM: Monad[G], FBMonoid: Monoid[F[B]]
  ): G[F[B]] = fas.flatMap(as => sequentialTraverse(as)(f))

  test("mapTraverseSequential") {

    mapTraverseSequential[List, Id, Int, Int](List(1, 2, 3))(_ * 2) shouldEqual List(2, 4, 6)

    def f(n: Int): Future[Int] = Future.successful(n)
    mapTraverseSequential[List, Future, Int, Int](Future.successful(List(1, 2, 3)))(n => f(n * 2)).futureValue shouldEqual List(2, 4, 6)
  }

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
