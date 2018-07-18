package defaultimplicit

import org.scalatest.{FunSuite, Matchers}

class DefaultImplicitResolutionTest extends FunSuite with Matchers {

  test("Scala implicit resolution finds some default instance") {
    MyTypeClass[Int].foo(666) should contain ("666")
    MyTypeClass[String].foo("1234") shouldBe empty
  }
}
