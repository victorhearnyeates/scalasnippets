package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._

class PrintCaseClassDataTest extends FunSuite with Matchers {

  case class Foo(i: Int, s: Option[String], b: Option[Boolean])

  test("Print stuff") {

    object polyPrint extends Poly1 {
      implicit def caseInt: Case.Aux[Int, Option[String]] = at[Int](i => Some(i.toString))
      implicit def caseStr: Case.Aux[String, Option[String]] = at[String](s => Some(s))
      implicit def caseBool: Case.Aux[Boolean, Option[String]] = at[Boolean](b => Some(b.toString))
      implicit def caseOpt[A](implicit ca: Case.Aux[A, Option[String]]): Case.Aux[Option[A], Option[String]] =
        at[Option[A]](_.flatMap(ca))
    }

    val foo = Foo(123, None, Some(true))
    val print: String = Generic[Foo].to(foo).map(polyPrint).toList.flatten.mkString("::")
    print shouldEqual "123::true"
  }
}
