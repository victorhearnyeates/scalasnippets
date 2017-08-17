package shapelesstests

import shapeless._

trait CsvEncoder[A] {
  def encode(a: A): List[String]
}

object CsvEncoder {

  def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] = enc

  implicit val stringEncoder: CsvEncoder[String] = s => List(s)
  implicit val intEncoder: CsvEncoder[Int] = i => List(i.toString)
  implicit val booleanEncoder: CsvEncoder[Boolean] = b => List(if (b) "yes" else "no")

  implicit val hnilEncoder: CsvEncoder[HNil] = _ => Nil

  implicit def hlistEncoder[H, T <: HList](implicit hEncoder: CsvEncoder[H], tEncoder: CsvEncoder[T]): CsvEncoder[H :: T] = {
    case h :: t => hEncoder.encode(h) ++ tEncoder.encode(t)
  }

  implicit val cnilEncoder: CsvEncoder[CNil] =
    _ => throw new RuntimeException("It is not possible to construct values of CNil type!")

  implicit def coproductEncoder[H, T <: Coproduct](implicit hEncoder: CsvEncoder[H], tEncoder: CsvEncoder[T]): CsvEncoder[H :+: T] = {
    case Inl(h) => hEncoder.encode(h)
    case Inr(t) => tEncoder.encode(t)
  }

  implicit def genericEncoder[A, R](implicit gen: Generic.Aux[A, R], encoder: CsvEncoder[R]): CsvEncoder[A] =
    a => encoder.encode(gen.to(a))
}
