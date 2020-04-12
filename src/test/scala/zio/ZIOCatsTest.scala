package zio

import cats.implicits._
import org.scalatest.{FunSuite, Matchers}
import zio.internal.Platform
import zio.interop.catz._

class ZIOCatsTest extends FunSuite with Matchers {

  val runtime = Runtime.default

  test("Traverse") {
    def task(i: Int): Task[Int] = Task.succeed(i)
    val ints: List[Int] = List(1, 2, 3)
    val traverse: Task[List[Int]] = ints.traverse(task)
    runtime.unsafeRun(traverse) shouldEqual List(1, 2, 3)
  }
}
