package typeconstraints

import org.scalatest.{FunSuite, Matchers}

class GeneralisedTypeConstraintsTest extends FunSuite with Matchers {

  test("Flatten an option") {

    def flatten[A, B](a: Option[A])(implicit ev: A <:< Option[B]): Option[B] =
      if (a.isEmpty) None else a.get

    flatten(Option(Option(123))) should === (Option(123))
  }

  test("Seq to Map") {

    def toMap[A, K, V](as: Seq[A])(implicit ev: A <:< (K, V)): Map[K, V] =
      Map.empty[K, V] ++ as.map(ev) // We can use the apply function of ev to construct a (K, V) from an A

    val pairs = Seq(("1", 1), ("2", 2), ("3", 3))

    toMap(pairs) should === (Map("1" -> 1, "2" -> 2, "3" -> 3))
  }
}
