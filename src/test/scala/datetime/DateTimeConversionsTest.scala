package datetime

import java.time.{ZoneId, ZonedDateTime}
import java.util.Date

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.{FunSuite, Matchers}

class DateTimeConversionsTest extends FunSuite with Matchers {

  test("Joda DateTime conversions") {

    // Note a Joda DateTime value represents an exact point on the time-line, but limited to the precision of milliseconds.
    // A DateTime calculates its fields with respect to a time zone.

    val London: DateTimeZone = DateTimeZone.forID("Europe/London")
    val dateTime: DateTime = new DateTime(2019, 5, 29, 11, 30, 0, London)

    val juDate: Date = dateTime.toDate
    val dateTimeFromJUDate: DateTime = new DateTime(juDate.getTime)
    dateTime.getMillis shouldEqual dateTimeFromJUDate.getMillis
  }

  test("Java ZonedDateTime conversions") {

    val London: ZoneId = ZoneId.of("Europe/London")

    val zonedDateTime: ZonedDateTime = ZonedDateTime.of(2019, 5, 29, 11, 30, 0, 0, London)
    val juDate: Date = Date.from(zonedDateTime.toInstant)

    zonedDateTime.toInstant shouldEqual juDate.toInstant
    val zonedDateTimeFromJUDate: ZonedDateTime = juDate.toInstant.atZone(London)
    zonedDateTimeFromJUDate.toInstant shouldEqual zonedDateTime.toInstant
  }
}
