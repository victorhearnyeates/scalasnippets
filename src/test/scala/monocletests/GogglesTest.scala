package monocletests

import org.scalatest.{FunSuite, Matchers}

class GogglesTest extends FunSuite with Matchers {

  test("Bakery Example") {

    import goggles._

    case class Topping(cherries: Int)
    case class Cake(toppings: List[Topping])
    case class Bakery(cakes: List[Cake])

    val myBakery = Bakery(List(
      Cake(List(Topping(0), Topping(3))),
      Cake(List(Topping(4))),
      Cake(Nil))
    )

    def cherries(b: Bakery): List[Int] = get"$b.cakes*.toppings[0].cherries"
    cherries(myBakery) should === (List(0, 4))
    val updatedBakery: Bakery = set"$myBakery.cakes*.toppings[0].cherries" := 7
    cherries(updatedBakery) should === (List(7, 7))
  }

  test("Interpolate any Monocle optic") {

    import goggles._
    import monocle.std.string.stringToInt
    import monocle.std.int.intToChar

    val c: Option[Char] = get"${"113"}.$stringToInt.$intToChar"
    c should contain ('q')

    val s = set"${"113"}.$stringToInt.$intToChar" ~= (_.toUpper)
    s should === ("81")
  }

  test("Traverse List *") {

    import goggles._

    case class Point(x: Double, y: Double)
    val polygon = List(Point(0.0, 0.0), Point(0.0, 1.0), Point(1.0, 1.0), Point(1.0, 0.0))
    get"$polygon*.x" should === (List(0.0, 0.0, 1.0, 1.0))
    (set"$polygon*.x" += 1.5) should === (List(Point(1.5, 0.0), Point(1.5, 1.0), Point(2.5, 1.0), Point(2.5, 0.0)))
  }

  test("Optional values ?") {

    import goggles._

    case class Estate(farm: Option[Farm])
    case class Farm(prizeChicken: Option[Chicken])
    case class Chicken(egg: Option[Egg])
    case class Egg(weight: Double)

    val estate = Estate(Some(Farm(Some(Chicken(Some(Egg(2.3)))))))
    get"$estate.farm?.prizeChicken?.egg?.weight" should === (Some(2.3))
    (set"$estate.farm?.prizeChicken?.egg?.weight" *= 2) should === (Estate(Some(Farm(Some(Chicken(Some(Egg(4.6))))))))
  }

  test("Select indexed values") {

    import goggles._

    sealed trait Square
    case object B extends Square
    case object X extends Square
    case object O extends Square

    val ticTac: Vector[Vector[Square]] = Vector(
      Vector(X, B, B),
      Vector(O, X, B),
      Vector(B, O, O)
    )

    get"$ticTac[0][0]" should === (Some(X))

    (set"$ticTac[2][0]" := O) should === (Vector(
      Vector(X, B, B),
      Vector(O, X, B),
      Vector(O, O, O)
    ))
  }
}
