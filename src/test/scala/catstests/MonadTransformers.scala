package catstests

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class MonadTransformers extends FunSuite with Matchers with ScalaFutures {

  test("OptionT") {

    import cats.data.OptionT
    import cats.instances.future._

    val fo1: Future[Option[Int]] = Future.successful(Option(1))

    val resultOptionT: OptionT[Future, Int] = for {
      one <- OptionT.apply[Future, Int](fo1)
      two <- OptionT.pure[Future](2)
      three <- OptionT.liftF[Future, Int](Future.successful(3))
    } yield one + two + three

    val resultFo: Future[Option[Int]] = resultOptionT.value
    resultFo.futureValue should contain (6)
  }

  test("EitherT") {

    import cats.data.EitherT
    import cats.instances.future._

    type ResultF[A] = EitherT[Future, String, A]

    val parseInt: Either[String, Int] = Try("1".toInt).toEither.left.map(_ => "Error")

    val result: ResultF[Int] = for {
      one <- EitherT.fromEither(parseInt)
      two <- EitherT.pure[Future, String](2)
      three <- EitherT.right[String](Future.successful(3))
    } yield one + two + three

    val resultFe: Future[Either[String, Int]] = result.value
    resultFe.futureValue should === (Right(6))
  }

  test("EitherT and Higher Kinded Types") {

    import cats._, cats.data._

    def add[F[_]: Monad](v: F[Int]): F[Either[String, Int]] = {

      val parseInt: Either[String, Int] = Try("1".toInt).toEither.left.map(_ => "Error")

      val result: EitherT[F, String, Int] = for {
        value <- EitherT.right[String](v)
        one <- EitherT.fromEither[F](parseInt)
        two <- EitherT.pure[F, String](2)
      } yield value + one + two

      result.value
    }

    add(3: Id[Int]) should === (Right(6))
  }
}
