package catstests

import org.scalatest.{FunSuite, Matchers}

class StateMonadTest extends FunSuite with Matchers {

  import cats.data.State

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
}
