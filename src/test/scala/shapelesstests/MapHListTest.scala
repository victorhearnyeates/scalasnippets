package shapelesstests

import org.scalatest.{FunSuite, Matchers}

class MapHListTest extends FunSuite with Matchers {

  case class Foo(name: String, age: Int, status: Boolean)
  case class Bar(a: String, b: Int)
  case class Baz(id: String, foo: Foo, bar: Bar)

  test("Tuple map") {

    import shapeless._
    import syntax.std.tuple._
    import cats.Show
    import cats.instances.all._

    val james = Foo("James", 45, true)

    def bar(f: Foo) = (
      "Name" -> f.name,
      "Age" -> f.age,
      "Status" -> f.status
    )

    object print extends Poly1 {
      implicit def default[A](implicit s: Show[A]): Case.Aux[(String, A), (String, String)] =
        at { case (h, a) => (h, s.show(a)) }
    }

    val headers: List[(String, String)] = bar(james).map(print).toList

    headers should === (List(
      "Name" -> "James",
      "Age" -> "45",
      "Status" -> "true"
    ))
  }
}
