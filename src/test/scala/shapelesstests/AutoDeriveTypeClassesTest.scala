package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._

class AutoDeriveTypeClassesTest extends FunSuite with Matchers {

  test("HList encoder") {
    CsvEncoder[String :: Int :: Boolean :: HNil].encode("abc" :: 123 :: true :: HNil) should === (List("abc", "123", "yes"))
  }

  test("IceCream specific encoder") {

    implicit val iceCreamEncoder: CsvEncoder[IceCream] = {
      val gen = Generic[IceCream]
      iceCream => CsvEncoder[gen.Repr].encode(gen.to(iceCream))
    }

    val iceCream = IceCream("Vanilla", 99, true)
    CsvEncoder[IceCream].encode(iceCream) should === (List("Vanilla", "99", "yes"))
  }

  test("Generic encoder") {

    val iceCream = IceCream("Vanilla", 99, true)
    CsvEncoder[IceCream].encode(iceCream) should === (List("Vanilla", "99", "yes"))

    val shapes: List[Shape] = List(Rectangle(3, 4), Circle(1))
    shapes.map(CsvEncoder[Shape].encode) should === (List(List("3", "4"), List("1")))
  }

  test("Generic csv reader 1") {
    val csv: List[String] = List("Vanilla", "99", "true")
    val (_, result) = CsvReader[IceCream].read(csv)
    result should contain (IceCream("Vanilla", 99, true))
  }

  test("Generic csv reader 2") {
    val csv: List[String] = List("James", "Reddick", "45", "true")
    val (_, result) = CsvReader[Registration].read(csv)
    result should contain (Registration(PersonName("James", "Reddick"), 45, true))
  }
}
