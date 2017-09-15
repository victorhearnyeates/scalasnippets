package shapelesstests

import org.scalatest.{FunSuite, Matchers}

class MapHListTest extends FunSuite with Matchers {

  test("Tuple map") {

    import shapeless._
    import syntax.std.tuple._

    case class Foo(name: String, age: Int, status: Boolean)

    val james = Foo("James", 45, true)

    def bar(f: Foo) = (
      "Name" -> f.name,
      "Age" -> f.age,
      "Status" -> f.status
    )

    object print extends Poly1 {
      implicit def default[A]: Case.Aux[(String, A), String] = at(_._1)
    }

    val headers: List[String] = bar(james).map(print).toList
    headers should === (List("Name", "Age", "Status"))
  }
}
