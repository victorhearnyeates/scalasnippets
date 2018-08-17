package catstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._
import cats.data.State

class StateMonadTest extends FunSuite with Matchers {

  case class FooBar(s: String, i: Int, b: Baz)
  case class Baz(ss: String)

  object FooBar {
    val sLens = lens[FooBar] >> 's
    val iLens = lens[FooBar] >> 'i
    val ssLens = lens[FooBar] >> 'b >> 'ss
  }

  object Stack {

    def push[A](a: A): State[List[A], Unit] = State[List[A], Unit] {
      stack => (a :: stack, ())
    }

    def push2[A](a: A): State[List[A], Unit] = State.modify(a :: _)

    def pop[A](): State[List[A], Option[A]] = State[List[A], Option[A]] {
      stack => (stack.tail, stack.headOption)
    }
  }

  test("Stack") {

    import Stack._

    val s1 = for {
      _ <- push2(1)
      _ <- push2(2)
      _ <- push2(3)
      three <- pop()
    } yield three

    s1.runA(Nil).value should contain (3)
    s1.runS(Nil).value should === (List(2, 1))
  }

  test("Shapeless Lens") {

    val fooBar = FooBar("s", 0, Baz("ss"))

    val prog: State[FooBar, Unit] = for {
      _ <- State.modify[FooBar](FooBar.sLens.set(_)("abc"))
      _ <- State.modify[FooBar](FooBar.iLens.set(_)(123))
      _ <- State.modify[FooBar](FooBar.ssLens.set(_)("xyz"))
    } yield ()

    val result: FooBar = prog.runS(fooBar).value
    result shouldEqual FooBar("abc", 123, Baz("xyz"))
  }
}
