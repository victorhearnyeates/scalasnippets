package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._

class ShapelessOpticsTest extends FunSuite with Matchers {

  case class Address(street: String, city: String)
  case class Person(name: String, age: Int, address: Address, pet: Option[Pet])

  sealed trait Pet
  case class Dog(name: String) extends Pet
  case class Cat(age: Int) extends Pet

  test("Shapeless Optics") {

    val mary = Person("Mary", 32, Address("Southover Street", "Brighton"), Some(Dog("Fido")))
    val streetLens = lens[Person].address.street

    val street = streetLens.get(mary)
    street shouldEqual "Southover Street"

    val mary2 = streetLens.set(mary)("Montpelier Road")
    mary2 shouldEqual Person("Mary", 32, Address("Montpelier Road", "Brighton"), Some(Dog("Fido")))

    val petNamePrism = prism[Person].pet.value.name
    val petAgePrism = prism[Person].pet.value.age

    petNamePrism.get(mary) shouldEqual Some("Fido")
    petAgePrism.get(mary) shouldEqual None

    val mary3 = petNamePrism.set(mary2)("Mutley")
    mary3 shouldEqual Person("Mary", 32, Address("Montpelier Road", "Brighton"), Some(Dog("Mutley")))

    val mary4 = petAgePrism.set(mary3)(99)
    mary4 shouldEqual Person("Mary", 32, Address("Montpelier Road", "Brighton"), Some(Dog("Mutley")))
  }
}
