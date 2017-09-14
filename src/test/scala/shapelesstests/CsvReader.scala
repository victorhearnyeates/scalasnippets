package shapelesstests

import shapeless._
import cats.Applicative
import cats.implicits._
import scala.util.Try

case class PersonName(firstName: String, lastName: String)
case class Registration(person: PersonName, age: Int, isValid: Boolean)

trait CsvReader[A] {
  def read(csv: List[String]): (List[String], Option[A])
}

object CsvReader {

  def apply[A](implicit reader: CsvReader[A]): CsvReader[A] = reader

  implicit val stringReader: CsvReader[String] = csv => (csv.tail, csv.headOption)
  implicit val intReader: CsvReader[Int] = csv => (csv.tail, csv.headOption.flatMap(v => Try(v.toInt).toOption))
  implicit val booleanReader: CsvReader[Boolean] = csv => (csv.tail, csv.headOption.map(_ == "true"))

  implicit val hnilReader: CsvReader[HNil] = csv => (csv, Some(HNil))

  implicit def hlistReader[H, T <: HList](implicit hReader: Lazy[CsvReader[H]], tReader: CsvReader[T]): CsvReader[H :: T] = csv => {
    val (t1, hOpt) = hReader.value.read(csv)
    val (t2, tOpt) = tReader.read(t1)
    val r: Option[H :: T] = Applicative[Option].map2(hOpt, tOpt)((h, t) => h :: t)
    (t2, r)
  }

  implicit def genericReader[A, R](implicit gen: Generic.Aux[A, R], reader: Lazy[CsvReader[R]]): CsvReader[A] = csv => {
    val (tail, rOpt) = reader.value.read(csv)
    val aOpt: Option[A] = rOpt.map(gen.from)
    (tail, aOpt)
  }
}