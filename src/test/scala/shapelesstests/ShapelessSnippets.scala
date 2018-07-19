package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._
import shapeless.ops.hlist.IsHCons

object ShapelessSnippets {

  def head[A, R <: HList, H, T <: HList](a: A)(
    implicit gen: Generic.Aux[A, R], isHCons: IsHCons.Aux[R, H, T]
  ): H = gen.to(a).head

  // Extra constraint to ensure a single value case class
  def getWrappedValue[A, R <: HList, H](a: A)(
    implicit gen: Generic.Aux[A, R], isHCons: IsHCons.Aux[R, H, HNil]
  ): H = gen.to(a).head
}

class ShapelessSnippetsTest extends FunSuite with Matchers {

  import ShapelessSnippets._

  test("Get wrapped value") {

    case class Foo(i: Int)
    val v: Int = head(Foo(123))
    v should be (123)

    case class Bar(i: Int, s: String)
    val v2: Int = head(Bar(123, "abc"))
    v2 should be (123)

    val v3: Int = getWrappedValue(Foo(123))
    v3 should be (123)
  }
}
