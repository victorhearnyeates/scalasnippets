package zio

import cats.{Foldable, Monad, Monoid, Id}
import cats.implicits._
import zio.interop.catz._
import org.scalatest.{FunSuite, Matchers}
import ZIOFolding._

class ZIOFolding extends FunSuite with Matchers {

  test("foldMapMUntil") {
    foldMapMUntil[Id, Int, Int](List(1, 2, 3, 4, 5), identity, _ > 2) shouldEqual 3
    foldMapMUntil[Id, Int, Int](List(1, 2, 3, 4, 5), identity, _ > 4) shouldEqual 6
    foldMapMUntil[Id, Int, Int](List(1, 2, 3, 4, 5), identity, _ > 8) shouldEqual 10
  }
}

object ZIOFolding {

  def foldMapMPar[A, B: Monoid](as: List[A], f: A => Task[B]): Task[B] =
    as.foldLeft[Task[B]](Task.effect(Monoid[B].empty)) { case (acc, nxt) =>
      acc.zipWithPar(f(nxt))(_ |+| _)
    }

  def foldMapMParNUntil[A, B: Monoid](as: List[A], f: A => Task[B], batchSize: Int, until: B => Boolean): Task[B] =
    Foldable[List].foldLeftM[Task, List[A], B](as.grouped(batchSize).toList, Monoid[B].empty) { case (acc, nxt) =>
      if (until(acc)) Task.effectTotal(acc) else foldMapMPar[A, B](nxt, f).map(_ |+| acc)
    }

  def foldMapMUntil[F[_]: Monad, A, B: Monoid](as: List[A], f: A => F[B], until: B => Boolean): F[B] =
    Foldable[List].foldLeftM[F, A, B](as, Monoid[B].empty) { case (acc, nxt) =>
      if (until(acc)) acc.pure[F] else f(nxt).map(_ |+| acc)
    }
}