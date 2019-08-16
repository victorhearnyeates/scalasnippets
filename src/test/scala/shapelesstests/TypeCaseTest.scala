package shapelesstests

import org.scalatest.{FunSuite, Matchers}
import shapeless._

class TypeCaseTest extends FunSuite with Matchers {

  import TypeCaseTest._

  test("TypeCase") {
    print("abc") shouldEqual "abc"
    print(123) shouldEqual "123"
    print(List(1, 2, 3)) shouldEqual "1,2,3"
    print(true) shouldEqual "None"
  }
}

object TypeCaseTest {

  def print[A: Typeable](a: A): String = {

    val string = TypeCase[String]
    val int = TypeCase[Int]
    val listInt = TypeCase[List[Int]]

    a match {
      case string(s) => s
      case int(i) => i.toString
      case listInt(is) => is.map(_.toString).mkString(",")
      case _ => "None"
    }
  }
}