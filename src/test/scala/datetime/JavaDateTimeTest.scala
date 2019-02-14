package datetime

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

import org.scalatest.{FunSuite, Matchers}

class JavaDateTimeTest extends FunSuite with Matchers {

  val London = ZoneId.of("Europe/London")

  test("stuff") {

    val dateTimeStr = "2019-04-05T11:24:00"
    val localDateTime: LocalDateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val zonedDateTime: ZonedDateTime = ZonedDateTime.of(localDateTime, London)

    localDateTime should not be null
    zonedDateTime should not be null
  }
}
