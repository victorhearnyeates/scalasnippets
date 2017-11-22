package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._

case class KVPair[A](key: String, value: A)

trait Mapper[A] {
  type B
  def map(a: A): B
}

object Mapper {

  def apply[A](implicit mapper: Mapper[A]): Mapper[A] = mapper

  implicit def kvMapper[A]: Mapper[KVPair[A]] = new Mapper[KVPair[A]] {
    type B = A
    def map(p: KVPair[A]): B = p.value
  }

  implicit val hnilMapper: Mapper[HNil] = new Mapper[HNil] {
    type B = HNil
    def map(hnil: HNil): HNil = HNil
  }

  implicit def hlistMapper[H, T <: HList](implicit hMapper: Mapper[H], tMapper: Mapper[T]): Mapper[H :: T] = {
    new Mapper[H :: T] {
      type B = hMapper.B :: HList
      def map(a: H :: T): B = tMapper.map(a.tail) match {
        case hlist: HList => hMapper.map(a.head) :: hlist
      }
    }
  }

  implicit def genericMapper[A, R](implicit gen: Generic.Aux[A, R], mapper: Mapper[R]): Mapper[A] = {
    new Mapper[A] {
      type B = mapper.B
      def map(a: A): B = mapper.map(gen.to(a))
    }
  }
}

class DepTypesTest extends FunSuite with Matchers {

  test("Stuff") {

    val hlist = KVPair("s", "String") :: KVPair("i", 123) :: HNil
    val expected1 = "String" :: 123 :: HNil
    Mapper[KVPair[String] :: KVPair[Int] :: HNil].map(hlist) should === (expected1)

    case class Foo(skvPair: KVPair[String], ikvPair: KVPair[Int], bkvPair: KVPair[Boolean])
    case class Bar(s: String, i: Int, b: Boolean)

    val foo = Foo(KVPair("s", "String"), KVPair("i", 123), KVPair("b", true))
    val expected2 = "String" :: 123 :: true :: HNil
    Mapper[Foo].map(foo) should === (expected2)

    // TODO SOrt this out!
    // val x: String :: Int :: Boolean :: HNil = Mapper[Foo].map(foo)
    // Generic[Bar].from(x)
  }
}
