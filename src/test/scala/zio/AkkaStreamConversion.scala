package zio

import scala.concurrent.Future

import akka.stream._
import akka.stream.scaladsl.{Sink => AkkaSink, Source => AkkaSource, _}
import cats.implicits.{none, _}
import zio.stream._

trait AkkaStreamConversionSyntax {

  implicit class AkkaSourceOps[A, M](source: Graph[SourceShape[A], M]) {
    def toZIOStream(implicit materializer: Materializer): Stream[Throwable, A] =
      AkkaStreamConversion.toZStream(source)
  }

  implicit class ZIOStreamOps[A](stream: Stream[Throwable, A]) {
    def writeToAkkaSink[M](sink: Graph[SinkShape[A], Future[M]])(implicit materializer: Materializer): Task[M] =
      AkkaStreamConversion.writeToAkkaSink(stream, sink)
  }
}

object AkkaStreamConversionSyntax extends AkkaStreamConversionSyntax

object AkkaStreamConversion {

  def toZStream[A, M](
    source: Graph[SourceShape[A], M],
    onMaterialization: M => Unit = (_: M) => ()
  )(implicit materializer: Materializer): Stream[Throwable, A] = {

    val createStream: Task[Stream[Throwable, A]] = Task.effect {
      val (mat, subscriber) = AkkaSource.fromGraph(source).toMat(AkkaSink.queue[A]())(Keep.both).run()
      onMaterialization(mat)
      subscriberStream[A](subscriber)
    }

    ZStream.fromEffect(createStream).flatMap(identity)
  }

  def writeToAkkaSink[A, M](
    stream: Stream[Throwable, A],
    sink: Graph[SinkShape[A], Future[M]]
  )(implicit materializer: Materializer): Task[M] = Task.effectSuspend {
    val (publisher, mat) = AkkaSource.queue[A](0, OverflowStrategy.backpressure).toMat(sink)(Keep.both).run()
    publisherStream[A](publisher, stream) *> Task.fromFuture(_ => mat)
  }

  private def subscriberStream[A](subscriber: SinkQueueWithCancel[A]): Stream[Throwable, A] = {
    val pull: Task[Option[A]] = Task.fromFuture(_ => subscriber.pull())
    val cancel: UIO[Unit] = UIO.effectTotal(subscriber.cancel())
    val finalise: Stream[Throwable, Unit] = ZStream.bracket[Any, Throwable, Unit](Task.unit)(_ => cancel)
    val run: Stream[Throwable, A] = ZStream.repeatEffect(pull).collectWhile{ case Some(a) => a }
    finalise.flatMap(_ => run)
  }

  private def publisherStream[A](
    publisher: SourceQueueWithComplete[A],
    stream: Stream[Throwable, A]
  ): Task[Unit] = {

    def publish(a: A): Task[Option[Unit]] = {
      val offer: Task[QueueOfferResult] = Task.fromFuture(_ => publisher.offer(a))
      offer.flatMap {
        case QueueOfferResult.Enqueued => Task.succeed(().some)
        case QueueOfferResult.Failure(cause) => Task.fail(cause)
        case QueueOfferResult.QueueClosed => Task.succeed(none[Unit])
        case QueueOfferResult.Dropped => Task.fail(new IllegalStateException("This should never happen because we use OverflowStrategy.backpressure"))
      }.catchSome {
        // This handles a race condition between `interruptWhen` and `publish`.
        // There's no guarantee that, when the akka sink is terminated, we will observe the
        // `interruptWhen` termination before calling publish one last time.
        // Such a call fails with StreamDetachedException
        case _: StreamDetachedException => Task.succeed(none[Unit])
      }
    }

    def watchCompletion: Task[Unit] = Task.fromFuture(_ => publisher.watchCompletion()).unit
    def fail(e: Throwable): Task[Unit] = Task.effect(publisher.fail(e)) *> watchCompletion
    val complete: Task[Unit] = Task.effect(publisher.complete()) *> watchCompletion
    val s: Stream[Throwable, Unit] = stream.mapM(publish).collectWhile{ case Some(_) => () }
    s.runDrain.foldM(fail, _ => complete)
  }
}
