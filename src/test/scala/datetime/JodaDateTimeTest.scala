package datetime

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.{FunSuite, Matchers}

class JodaDateTimeTest extends FunSuite with Matchers {

  val London: DateTimeZone = DateTimeZone.forID("Europe/London")

  test("Two DateTime values can have the same Instant but are not equal") {

    val dt1 = new DateTime(2008, 6, 1, 14, 0, 0)
    val dateTimeStr = "2008-06-01T14:00:00+01:00"
    val dt2 = DateTime.parse(dateTimeStr)

    // This shows that even though these two dates have the same time instance,
    // it is possible for the DateTime value to be different!
    // Instant represents an exact point on the time-line, but limited to a precision of milliseconds.
    dt1.toInstant shouldEqual dt2.toInstant
    dt1 should not equal dt2
  }
}
