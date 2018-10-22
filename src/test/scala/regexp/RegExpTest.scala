package regexp

import scala.util.matching.Regex

import org.scalatest.{FunSuite, Matchers}

class RegExpTest extends FunSuite with Matchers {

  test("Exclude chars from a string") {

    def exclude(c: Char): Boolean = {
      val p: Regex = """\s|/|\\|-|\[|\]|\{|\}|\*|\?|'|"""".r
      p.findFirstIn(c.toString).nonEmpty
    }

    "a-b{c*d".filterNot(exclude) shouldEqual "abcd"
  }
}
