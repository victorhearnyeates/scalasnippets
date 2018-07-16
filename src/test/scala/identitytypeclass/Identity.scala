package identitytypeclass

import cats._
import cats.implicits._

case class Foo(i: Int, s: String)

object Foo {

  implicit val fooEquals: Eq[Foo] = Eq.fromUniversalEquals[Foo]

  // Aux Pattern - do not add type annotation to this implicit!
  implicit val fooIdentity = new Identity[Foo] {
    type B = Int
    def identity(foo: Foo): Int = foo.i
  }
}

trait Identity[A] {
  type B
  def identity(a: A): B
}

object Identity {

  type Aux[A0, B0] = Identity[A0] { type B = B0 }

  def findById[A, ID](as: Seq[A], id: ID)(implicit identity: Identity.Aux[A, ID], equals: Eq[ID]): Option[A] =
    as.find(a => identity.identity(a) === id)
}
