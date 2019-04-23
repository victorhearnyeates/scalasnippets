package fastparsetests

import org.joda.time.DateTime
import org.scalatest.{EitherValues, FunSuite, Matchers}

class FastParseTest extends FunSuite with Matchers with EitherValues {

  import fastparse._, NoWhitespace._

  def toEither[A](p: Parsed[A], errorMsg: => String): Either[String, A] = p match {
    case s: Parsed.Success[A] => Right(s.value)
    case _: Parsed.Failure => Left(errorMsg)
  }

  test("Simple CSV example") {
    def commaSeperator[_: P]: P[Unit] = P(" ".rep ~ "," ~ " ".rep)
    def parser[_: P]: P[Seq[String]] = P(AnyChar.!.rep(sep = commaSeperator))
    val r: Parsed[Seq[String]] = parse("a ,  a,  a", parser(_))
    val Parsed.Success(csv, _) = r
    csv should === (Seq("a", "a", "a"))
  }

  test("Parse CSV1") {

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
