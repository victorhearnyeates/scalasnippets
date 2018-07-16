package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._
import shapeless.ops.hlist.Tupler

case class IceCreamParlour(name: String, age: Int, iceCream: IceCream, active: Boolean)

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
    implicit hProjection: Lazy[Projection.Aux[H, H0]],
    tProjection: Projection.Aux[T, T0]
  ) = new Projection[H :: T] {
    type B = H0 :: T0
    def write(hlist: H :: T): H0 :: T0 = hProjection.value.write(hlist.head) :: tProjection.write(hlist.tail)
  }

  implicit def genericProjection[A, R, R0 <: HList](
    implicit gen: Lazy[Generic.Aux[A, R]],
    projection: Projection.Aux[R, R0],
    tupler: Tupler[R0]
  ) = new Projection[A] {
    type B = tupler.Out
    def write(a: A): tupler.Out = tupler(projection.write(gen.value.to(a)))
  }
}

class DepTypesTest2 extends FunSuite with Matchers {

  test("stuff") {

    val iceCream: IceCream = IceCream("Sundae", 1, false)

    val x: (String, Int, String) = Projection.write(iceCream)
    x should be (("Sundae", 1, "false"))

    val iceCreamParlour = IceCreamParlour("Rossis", 100, iceCream, true)
    val y: (String, Int, (String, Int, String), String) = Projection.write(iceCreamParlour)
    y should be (("Rossis", 100, ("Sundae", 1, "false"), "true"))
  }
}
