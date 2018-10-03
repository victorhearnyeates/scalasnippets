package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._

class ShapelessOpticsTest extends FunSuite with Matchers {

  case class Address(street: String, city: String)
  case class Pet(name: String)
  case class Person(name: String, age: Int, address: Address, pet: Option[Pet] = None)

  test("Shapeless Optics") {

    val mary = Person("Mary", 32, Address("Southover Street", "Brighton"), Some(Pet("Fido")))
    val streetLens = lens[Person].address.street

    val street = streetLens.get(mary)
    street shouldEqual "Southover Street"

    val mary2 = streetLens.set(mary)("Montpelier Road")
    mary2 shouldEqual Person("Mary", 32, Address("Montpelier Road", "Brighton"), Some(Pet("Fido")))
  }
}
