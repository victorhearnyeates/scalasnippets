package zio

import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow => AkkaFlow, Sink => AkkaSink, Source => AkkaSource, _}
import akka.testkit.TestProbe
import org.scalatest.{FunSuite, Matchers}
import zio.AkkaStreamConversion._
import zio.AkkaStreamConversionSyntax._
import zio.stream._

class AkkaToZIOStreamTest extends FunSuite with Matchers {

  val runtime = new DefaultRuntime {}
  implicit val system: ActorSystem = ActorSystem("AkkaToZIOStreamTest")
  val numbers: List[Int] = (1 to 10).toList

  test("Akka Stream source to a ZIO Stream") {
    val source = AkkaSource(numbers)
    val zStream: Stream[Throwable, Int] = source.toZIOStream
    val task: Task[List[Int]] = zStream.run(Sink.collectAll[Int])
    runtime.unsafeRun(task) shouldEqual numbers
  }

  test("Akka Stream source to a ZIO Stream propagates errors to stream as expected") {
    val error = new RuntimeException("Boom!")
    val source = AkkaSource(numbers) ++ AkkaSource.failed(error)
    val zStream: Stream[Throwable, Int] = source.toZIOStream
    val task: Task[List[Int]] = zStream.run(Sink.collectAll[Int])
    runtime.unsafeRunSync(task).succeeded shouldBe false
  }

  test("Akka Stream source to ZIO Stream propagates errors to source as expected") {

    val probe = TestProbe()
    val error = new RuntimeException("Boom!")
    val source = AkkaSource(numbers).watchTermination()(Keep.right)
    val zStream: Stream[Throwable, Int] =
      toZStream[Int, Future[Done]](source, mat => mat.onComplete(probe.ref ! _)) ++ Stream.fail(error)

    val task: Task[List[Int]] = zStream.run(Sink.collectAll[Int])
    runtime.unsafeRunSync(task).succeeded shouldBe false
    probe.expectMsg(Success(Done))
  }

  test("Akka Stream source to a ZIO Stream propagates early termination") {
    val probe = TestProbe()
    val source = AkkaSource(numbers).watchTermination()(Keep.right)
    val zStream: Stream[Throwable, Int] = toZStream[Int, Future[Done]](source, mat => mat.onComplete(probe.ref ! _)).take(3)
    val task: Task[List[Int]] = zStream.run(Sink.collectAll[Int])
    runtime.unsafeRun(task) shouldEqual numbers.take(3)
    probe.expectMsg(Success(Done))
  }


  test("ZIO Stream to Akka Sink") {
    val akkaSink = AkkaSink.seq[Int]
    val zStream: Stream[Throwable, Int] = ZStream.fromIterable(numbers)
    val task: Task[Seq[Int]] = zStream.writeToAkkaSink(akkaSink)
    runtime.unsafeRun(task) shouldEqual numbers
  }

  test("ZIO Stream to Akka Sink propagates errors as expected 1") {
    val akkaSink = AkkaSink.seq[Int]
    val error = new RuntimeException("Boom")
    val zStream: Stream[Throwable, Int] = ZStream.fail(error)
    val task: Task[Unit] = zStream.writeToAkkaSink(akkaSink).unit
    val run: IO[Boolean, Boolean] = task.either.collect(false) { case Left(e) if e == error => true }
    runtime.unsafeRun(run) shouldBe true
  }

  test("ZIO Stream to Akka Sink propagates errors as expected 2") {
    val akkaSink = AkkaSink.seq[Int]
    val error = new RuntimeException("Boom")
    val zStream: Stream[Throwable, Int] = Stream.fromEffect(Task.fail(error))
    val task: Task[Unit] = zStream.writeToAkkaSink(akkaSink).unit
    val run: IO[Boolean, Boolean] = task.either.collect(false) { case Left(e) if e == error => true }
    runtime.unsafeRun(run) shouldBe true
  }

  test("ZIO Stream to Akka Sink propagates early termination due to error") {
    val error = new RuntimeException("Boom")
    val akkaSink = AkkaSink.foreach[Int](_ => throw error)
    val zStream: Stream[Throwable, Int] = ZStream.fromIterable(numbers)
    val task: Task[Unit] = zStream.writeToAkkaSink(akkaSink).unit
    runtime.unsafeRunSync(task).succeeded shouldBe false
  }

  test("ZIO Stream to Akka Sink propagates early termination") {
    val akkaSink = AkkaFlow[Int].take(3).toMat(AkkaSink.seq)(Keep.right)
    val zStream: Stream[Throwable, Int] = ZStream.fromIterable(numbers)
    val task: Task[Seq[Int]] = zStream.writeToAkkaSink(akkaSink)
    runtime.unsafeRun(task) shouldEqual numbers.take(3)
  }
}
