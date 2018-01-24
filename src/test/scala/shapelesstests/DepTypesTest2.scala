package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._
import shapeless.ops.hlist.Tupler

trait Foo[A] {
  type B
  def value: B
}

object Foo {

  def foo[T](t: T)(implicit f: Foo[T]): f.B = f.value

  implicit def fooInt = new Foo[Int] {
    override type B = String
    override def value = "Hey!"
  }

  implicit def fooString = new Foo[String] {
    override type B = Boolean
    override def value = true
  }
}

trait Projection[A] {
  type B
  def write(a: A): B
}

object Projection {

  def write[A](a: A)(implicit projection: Projection[A]): projection.B = projection.write(a)

  type Aux[A, B0] = Projection[A] { type B = B0 }

  // def apply[A](implicit projection: Projection[A]): Projection[A] = projection

  def default[A] = new Projection[A] {
    type B = A
    def write(a: A): B = a
  }

  implicit val stringProjection = default[String]
  implicit val intProjection = default[Int]

  implicit val boolProjection = new Projection[Boolean] {
    type B = String
    def write(b: Boolean): String = b.toString
  }

  implicit val hnilProjection = default[HNil]

  implicit def hlistProjection[H, H0, T <: HList, T0 <: HList](
    implicit hProjection: Projection.Aux[H, H0],
    tProjection: Projection.Aux[T, T0]
  ): Projection.Aux[H :: T, H0 :: T0] = new Projection[H :: T] {
    type B = H0 :: T0
    def write(hlist: H :: T): H0 :: T0 = hProjection.write(hlist.head) :: tProjection.write(hlist.tail)
  }

  implicit def genericProjection[A, R, R0 <: HList](
    implicit gen: Generic.Aux[A, R],
    projection: Projection.Aux[R, R0],
    tupler: Tupler[R0]
  ) = new Projection[A] {
    type B = tupler.Out
    def write(a: A): tupler.Out = tupler(projection.write(gen.to(a)))
  }
}

class DepTypesTest2 extends FunSuite with Matchers {

  test("stuff") {

    val a: String = Foo.foo(1) // "Hey!"
    val b: Boolean = Foo.foo("qwe") // true

    val y: String = Projection.write("abc")

    val iceCream: IceCream = IceCream("Sundae", 1, false)
    val repr: String :: Int :: Boolean :: HNil = Generic[IceCream].to(iceCream)

    val x: (String, Int, String) = Projection.write(iceCream)
    x should be (("Sundae", 1, "False"))

  }
}
