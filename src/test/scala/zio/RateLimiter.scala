package zio

import zio._
import cats.implicits._
import zio.interop.catz._

object RateLimiter {
  def create: UIO[RateLimiter] = Semaphore.make(1).map(new RateLimiter(_))
}

class RateLimiter private(semaphore: Semaphore) {
  def rateLimit[R, E, A](task: ZIO[R, E, A]): ZIO[R, E, A] = semaphore.withPermit(task)
}

object RateLimiter2 {
  def create: UIO[RateLimiter2] = Queue.bounded[Unit](1).map(new RateLimiter2(_))
}

class RateLimiter2 private(q: Queue[Unit]) {

  def rateLimit[A](task: => Task[A]): Task[A] = for {
    _ <- q.offer(())
    a <- task
    _ <- q.take
  } yield a
}

object RateLimiter3 {

  def create: UIO[RateLimiter3] = (
    Queue.bounded[Unit](1),
    Queue.bounded[Unit](1)
  ).mapN(new RateLimiter3(_, _))
}

class RateLimiter3 private(highPriorityQueue: Queue[Unit], lowPriorityQueue: Queue[Unit]) {

  private def queue(highPriority: Boolean): Queue[Unit] = if (highPriority) highPriorityQueue else lowPriorityQueue
  private def take(highPriority: Boolean): UIO[Unit] = wait(highPriority) *> queue(highPriority).take

  // In the case of a low priority task, this function will block until the high priority queue is empty.
  private def wait(highPriority: Boolean): UIO[Unit] =
    if (highPriority) UIO.unit else UIO.unit.doUntilM(_ => highPriorityQueue.size.map(_ == 0))

  def rateLimit[A](task: => Task[A], highPriority: Boolean): Task[A] = for {
    _ <- queue(highPriority).offer(())
    _ <- wait(highPriority)
    a <- task
    _ <- take(highPriority)
  } yield a
}
