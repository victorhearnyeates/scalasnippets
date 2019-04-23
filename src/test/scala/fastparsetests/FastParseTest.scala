package fastparsetests

import org.joda.time.DateTime
import org.scalatest.{EitherValues, FunSuite, Matchers}

class FastParseTest extends FunSuite with Matchers with EitherValues {

  import fastparse._

  def toEither[A](p: Parsed[A], errorMsg: => String): Either[String, A] = p match {
    case s: Parsed.Success[A] => Right(s.value)
    case _: Parsed.Failure => Left(errorMsg)
  }

  test("Parsing an html table") {

    import NoWhitespace._

    def startTag[_: P](name: String): P[Unit] = P("<" ~ name ~ (!">" ~ AnyChar).rep ~ ">")
    def endTag[_: P](name: String): P[Unit] = P("</" ~ name ~ ">")
    def stringBetween[_: P](tag: String): P[String] = P(startTag(tag) ~ (!endTag(tag) ~ AnyChar).rep.! ~ endTag(tag))

    def cell[_: P]: P[String] = P(stringBetween("td"))
    def cells[_: P]: P[Seq[String]] = P((!cell ~ AnyChar).rep ~ cell).rep
    def row[_: P]: P[Seq[String]] = P(startTag("tr") ~ cells ~ endTag("tr"))
    def rows[_: P]: P[Seq[Seq[String]]] = P((!row ~ AnyChar).rep ~ row).rep
    def table[_: P]: P[Seq[Seq[String]]] = P(startTag("table") ~ rows ~ endTag("table"))
    def tables[_: P]: P[Seq[Seq[Seq[String]]]] = P((!table ~ AnyChar).rep ~ table).rep

    parse("<td foo=bar>", startTag("td")(_)).isSuccess shouldBe true
    parse("<td foo=bar>foo bar</td>", stringBetween("td")(_)).get.value shouldEqual "foo bar"
    parse(Data.cells, cells(_)).get.value shouldEqual Seq("1", "2", "3", "4")

    val getTables: Seq[Seq[Seq[String]]] = parse(Data.html, tables(_)).get.value
    getTables should not be empty
  }

  test("Simple CSV example") {

    import NoWhitespace._

    def commaSeperator[_: P]: P[Unit] = P(" ".rep ~ "," ~ " ".rep)
    def parser[_: P]: P[Seq[String]] = P(AnyChar.!.rep(sep = commaSeperator))
    val r: Parsed[Seq[String]] = parse("a ,  a,  a", parser(_))
    val Parsed.Success(csv, _) = r
    csv should === (Seq("a", "a", "a"))
  }

  test("Multi line example") {

    import NoWhitespace._

    def prefix[_: P]: P[Unit] = P(CharsWhile(_ != '>'))
    def alpha[_: P]: P[Unit] = P(CharIn("a-z").rep)
    def get[_: P]: P[String] = P(">" ~ alpha.! ~ "<")
    def parser[_: P]: P[Seq[String]] = P(prefix ~ get).rep

    val data: String = Seq[String](
      "a>bc<defg",
      "abc>defg<",
      ">abcdefg<",
    ).mkString("\n")

    val r: Parsed[Seq[String]] = parse(data, parser(_))
    val Parsed.Success(result, _) = r
    result should contain allOf ("bc", "defg", "abcdefg")
  }

  test("Parse CSV1") {

    import NoWhitespace._

    case class ClubAdminRole(name: String, clubCode: String)
    case class Person(firstName: String, lastName: String, dob: DateTime, role: ClubAdminRole)

    val goodData = "James, Reddick, 05/04/1972, Club Admin (POLL)"
    val badData = "James, Reddick, 05/0A/1972, Club Admin (POLL)"

    def comma[_: P]: P[Unit] = P(" ".rep ~ "," ~ " ".rep)
    def alpha[_: P]: P[Unit] = P(CharIn("a-zA-Z").rep)

    def slash[_: P]: P[Unit] = P("/")
    def day[_: P]: P[Int] = P(CharIn("0-9").rep(min = 1, max = 2).!.map(_.toInt))
    def month[_: P]: P[Int] = day
    def year[_: P]: P[Int] = P(CharIn("0-9").rep(exactly = 4).!.map(_.toInt))

    def date[_: P]: P[DateTime] = P(day ~ slash ~ month ~ slash ~ year) map {
      case (d, m, y) => new DateTime(y, m, d, 0, 0)
    }

    def name[_: P]: P[String] = P(alpha ~ " " ~ alpha).!
    def code[_: P]: P[String] = P("(" ~ alpha.! ~ ")")

    def clubAdminRole[_: P]: P[ClubAdminRole] = P(name ~ " " ~ code).map(ClubAdminRole.tupled)

    def person[_: P]: P[Person] =
      P(alpha.! ~ comma ~ alpha.! ~ comma ~ date ~ comma ~ clubAdminRole).map(Person.tupled)

    val errorMsg = "Error reading data"

    val result1: Either[String, Person] = toEither(parse(goodData, person(_)), errorMsg)
    result1.right.value shouldEqual Person("James", "Reddick", new DateTime(1972, 4, 5, 0, 0), ClubAdminRole("Club Admin", "POLL"))

    val result2: Either[String, Person] = toEither(parse(badData, person(_)), errorMsg)
    result2.left.value shouldEqual "Error reading data"
  }
}
