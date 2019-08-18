package identitytypeclass

import scala.collection.immutable.Seq
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

  import IdentitySyntax._

  def findById[A, ID](as: Seq[A], id: ID)(implicit identity: Identity.Aux[A, ID], equals: Eq[ID]): Option[A] =
    as.find(_.id === id)
}

object IdentitySyntax {

  implicit class IdentityOps[A](a: A) {
    def id[ID](implicit identity: Identity.Aux[A, ID]): ID = identity.identity(a)
  }
}