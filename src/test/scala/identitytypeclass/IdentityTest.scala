package identitytypeclass

import org.scalatest.{FunSuite, Matchers}
import cats.implicits._

class IdentityTest extends FunSuite with Matchers {

  test("Find by id") {
    val foos: Seq[Foo] = Seq(Foo(1, "1"), Foo(2, "2"))
    Identity.findById(foos, 2) should contain (Foo(2, "2"))
  }
}
