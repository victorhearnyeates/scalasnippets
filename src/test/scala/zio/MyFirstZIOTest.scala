package zio

import zio.test._
import Assertion._

object MyFirstZIOTest extends DefaultRunnableSpec {

  val test1 = test("Test 1") {
    assert(3)(isGreaterThan(1))
  }

  def spec = suite("My first simple ZIO test")(test1)
}
