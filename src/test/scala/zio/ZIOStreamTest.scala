package zio

import org.scalatest.{FunSuite, Matchers}
import zio.internal.Platform
import zio.stream._

class ZIOStreamTest extends FunSuite with Matchers {

  val runtime = Runtime.default

  def addOne(i: Int): UIO[Int] = ZIO.succeed {
    Thread.sleep(i * 500)
    println(s"$i + 1")
    i + 1
  }

  test("mapMPar - mapping over elements and executing N concurrently") {

    var output: List[Int] = Nil
    def write(i: Int): UIO[Unit] = ZIO.succeed((output = output :+ i))

    val ints: List[Int] = List(4, 3, 2, 1)
    val s1: Stream[Nothing, Int] = ZStream.fromIterable(ints)
    val task: UIO[Unit] = s1.mapMPar(4)(addOne).foreach(write)

    val start = System.currentTimeMillis()
    runtime.unsafeRun(task)
    val end = System.currentTimeMillis()

    val duration = end - start
    println(duration)
    output should not be empty
  }

  test("flatMap") {

    val s1: Stream[Nothing, Int] = ZStream(1, 2, 3)
    val s2: Stream[Nothing, Int] = ZStream(4)
    val s: Stream[Nothing, Int] = s2.flatMap(_ => s1)
    val t: UIO[List[Int]] = s.run(Sink.collectAll[Int])
    runtime.unsafeRun(t) shouldEqual List(1, 2, 3)
  }
}
