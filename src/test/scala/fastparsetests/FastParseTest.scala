package fastparsetests

import org.joda.time.DateTime
import org.scalatest.{FunSuite, Matchers}

class FastParseTest extends FunSuite with Matchers {

  import fastparse.all._

  def toEither[A](p: Parsed[A]): Either[String, A] = p match {
    case s: Parsed.Success[A] => Right(s.value)
    case f: Parsed.Failure =>
      Left(f.lastParser.toString)
  }

  test("Simple CSV example") {
    val commaSeperator: Parser[Unit] = P(" ".rep ~ "," ~ " ".rep)
    val parser: Parser[Seq[String]] = P(AnyChar.!.rep(sep = commaSeperator))
    val r: Parsed[Seq[String]] = parser.parse("a ,  a,  a")
    val Parsed.Success(csv, _) = r
    csv should === (Seq("a", "a", "a"))
  }

  test("Parse CSV1") {

    case class ClubAdminRole(name: String, clubCode: String)
    case class Person(firstName: String, lastName: String, dob: DateTime, role: ClubAdminRole)

    val goodData = "James, Reddick, 05/04/1972, Club Admin (POLL)"
    val badData = "James, Reddick, 05/0A/1972, Club Admin (POLL)"

    val comma: Parser[Unit] = P(" ".rep ~ "," ~ " ".rep)
    val alpha: Parser[Unit] = P(CharIn(('a' to 'z') ++ ('A' to 'Z')).rep)

    val date: Parser[DateTime] = {

      val slash: Parser[Unit] = P("/")
      val day: Parser[Int] = P(CharIn('0' to '9').rep(min = 1, max = 2).!.map(_.toInt))
      val month = day
      val year: Parser[Int] = P(CharIn('0' to '9').rep(exactly = 4).!.map(_.toInt))

      val r = P(day ~ slash ~ month ~ slash ~ year) map {
        case (d, m, y) => new DateTime(y, m, d, 0, 0)
      }

      r.opaque("Error reading dob")
    }

    val clubAdminRole: Parser[ClubAdminRole] = {
      val name: Parser[String] = P(alpha ~ " " ~ alpha).!
      val code: Parser[String] = P("(" ~ alpha.! ~ ")")
      P(name ~ " " ~ code).map(ClubAdminRole.tupled)
    }

    val person: Parser[Person] = P(alpha.! ~ comma ~ alpha.! ~ comma ~ date ~ comma ~ clubAdminRole).map(Person.tupled)

    val result1: Either[String, Person] = toEither(person.parse(goodData))
    result1.contains[Person](Person("James", "Reddick", new DateTime(1972, 4, 5, 0, 0), ClubAdminRole("Club Admin", "POLL"))) shouldBe true

    val result2: Either[String, Person] = toEither(person.parse(badData))
    result2.swap.contains[String]("Error reading dob") shouldBe true
  }
}
