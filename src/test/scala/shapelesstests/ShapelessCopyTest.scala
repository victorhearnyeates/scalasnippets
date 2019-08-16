package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._

class ShapelessCopyTest extends FunSuite with Matchers {

  import ShapelessCopyTest._
  import copySyntax._

  test("Copy function for sealed family of case classes") {

    val b1: Base = Foo(23, true)
    b1.copy(i = 13) shouldEqual Foo(13, true)

    val b2: Base = Bar(23, "foo", false)
    b2.copy(i = 13, b = true) shouldEqual Bar(13, "foo", true)
  }
}

object ShapelessCopyTest {

  sealed trait Base
  case class Foo(i: Int, b: Boolean) extends Base
  case class Bar(i: Int, s: String, b: Boolean) extends Base
}

object copySyntax {

  class CopySyntax[T](t: T) {
    object copy extends RecordArgs {
      def applyRecord[R <: HList](r: R)(implicit update: UpdateRepr[T, R]): T = update(t, r)
    }
  }

  import scala.language.implicitConversions
  implicit def apply[T](t: T): CopySyntax[T] = new CopySyntax[T](t)
}

trait UpdateRepr[T, R <: HList] {
  def apply(t: T, r: R): T
}

object UpdateRepr {

  import ops.record._

  implicit def mergeUpdateRepr[T <: HList, R <: HList](implicit merger: Merger.Aux[T, R, T]): UpdateRepr[T, R] =
      (t: T, r: R) => merger(t, r)

  implicit def cnilUpdateRepr[R <: HList]: UpdateRepr[CNil, R] = (t: CNil, _: R) => t

  implicit def cconsUpdateRepr[H, T <: Coproduct, R <: HList](
    implicit uh: Lazy[UpdateRepr[H, R]],
    ut: Lazy[UpdateRepr[T, R]]
  ): UpdateRepr[H :+: T, R] = (t: H :+: T, r: R) => t match {
    case Inl(head) => Inl(uh.value(head, r))
    case Inr(tail) => Inr(ut.value(tail, r))
  }

  implicit def genProdUpdateRepr[T, R <: HList, Repr <: HList](
    implicit gen: LabelledGeneric.Aux[T, Repr],
    update: Lazy[UpdateRepr[Repr, R]]
  ): UpdateRepr[T, R] = (t: T, r: R) => gen.from(update.value(gen.to(t), r))

  implicit def genCoprodUpdateRepr[T, R <: HList, Repr <: Coproduct](
    implicit gen: Generic.Aux[T, Repr],
    update: Lazy[UpdateRepr[Repr, R]]
  ): UpdateRepr[T, R] = (t: T, r: R) => gen.from(update.value(gen.to(t), r))
}