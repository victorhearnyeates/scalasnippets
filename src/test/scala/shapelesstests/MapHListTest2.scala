package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._

case class KVPair[A](key: String, value: A)

object Mapper {

  object value extends Poly1 {
    implicit def kvPairCase[A]: Case.Aux[KVPair[A], A] = at(_.value)
  }
}

class DepTypesTest extends FunSuite with Matchers {

  test("Stuff") {

    val hlist = KVPair("s", "String") :: KVPair("i", 123) :: HNil
    val expected = "String" :: 123 :: HNil
    val r: String :: Int :: HNil = hlist.map(Mapper.value)
  }
}
