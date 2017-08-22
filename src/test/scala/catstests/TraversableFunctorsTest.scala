package catstests

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TraversableFunctorsTest extends FunSuite with Matchers with ScalaFutures {

  test("traverse") {

    import cats.Traverse
    import cats.instances.list._
    import cats.instances.future._
    import cats.instances.option._

    val traverse: Future[List[Int]] =
      Traverse[List].traverse[Future, Int, Int](List(1, 2))(Future.successful)

    traverse.futureValue should === (List(1, 2))

    val flatTraverse: Future[Option[Int]] =
      Traverse[Option].flatTraverse[Future, Int, Int](Option(99))(i => Future.successful(Option(i)))

    flatTraverse.futureValue should === (Option(99))
  }
}
