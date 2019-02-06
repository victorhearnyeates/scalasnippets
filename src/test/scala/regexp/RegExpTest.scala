package regexp

import scala.util.Try
import scala.util.matching.Regex

import org.scalatest.{FunSuite, Matchers}

class RegExpTest extends FunSuite with Matchers {

  test("Match a substring") {

    val str1 = """ attachment; filename="foo.bar" """.trim
    val str2 = """ nomatch; filename="foo.bar" """.trim
    val regexp: Regex = "attachment; filename=\"(.*)\"".r

    val filename1: Option[String] = Try{ val regexp(n) = str1; n }.toOption
    val filename2: Option[String] = Try{ val regexp(n) = str2; n }.toOption

    filename1 should contain ("foo.bar")
    filename2 shouldBe empty
  }

  test("Exclude chars from a string") {

    def exclude(c: Char): Boolean = {
      val p: Regex = """\s|/|\\|-|\[|\]|\{|\}|\*|\?|'|"""".r
      p.findFirstIn(c.toString).nonEmpty
    }

    "a-b{c*d".filterNot(exclude) shouldEqual "abcd"
  }
}
