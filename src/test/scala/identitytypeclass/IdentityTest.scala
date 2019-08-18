package identitytypeclass

import scala.collection.immutable.Seq
import org.scalatest.{FunSuite, Matchers}
import cats.implicits._

class IdentityTest extends FunSuite with Matchers {

  test("Identity") {

    import IdentitySyntax._

    val f = Foo(123, "123")
    val id: Int = f.id
    id shouldEqual 123
  }

  test("Find by id") {
    val foos: Seq[Foo] = Seq(Foo(1, "1"), Foo(2, "2"))
    Identity.findById(foos, 2) should contain (Foo(2, "2"))
  }
}
