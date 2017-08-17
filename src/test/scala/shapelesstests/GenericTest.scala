package shapelesstests

import org.scalatest.{FunSuite, Matchers}

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

sealed trait Shape
final case class Rectangle(w: Int, h: Int) extends Shape
final case class Circle(r: Int) extends Shape

class GenericTest extends FunSuite with Matchers {

  test("Generic product encoding") {

    import shapeless._

    val iceCream: IceCream = IceCream("Sundae", 1, false)
    val repr: String :: Int :: Boolean :: HNil = Generic[IceCream].to(iceCream)

    val iceCream2: IceCream = Generic[IceCream].from(repr)
    iceCream2 should === (iceCream)

    val hlist: String :: Int :: Boolean :: HNil = "Sundae" :: 1 :: false :: HNil
    Generic[IceCream].from(hlist) should === (iceCream)
  }

  test("Generic coproduct encoding") {

    import shapeless._

    val rectangle: Rectangle = Rectangle(3, 4)
    val circle: Circle = Circle(1)

    val rectangleRepr: Circle :+: Rectangle :+: CNil = Generic[Shape].to(rectangle)
    val circleRepr: Circle :+: Rectangle :+: CNil = Generic[Shape].to(circle)

    val rectangle2: Shape = Generic[Shape].from(rectangleRepr)
    rectangle2 should === (rectangle)

    val circle2: Shape = Generic[Shape].from(circleRepr)
    circle2 should === (circle)
  }
}
