package zio

import zio._

object RateLimiter {
  def create: UIO[RateLimiter] = Semaphore.make(1).map(new RateLimiter(_))
}

class RateLimiter private(semaphore: Semaphore) {
  def rateLimit[A](task: Task[A]): Task[A] = semaphore.withPermit(task)
}

object RateLimiter2 {
  def create: UIO[RateLimiter2] = Queue.bounded[Unit](1).map(new RateLimiter2(_))
}

class RateLimiter2(q: Queue[Unit]) {

  def rateLimit[A](task: => Task[A]): Task[A] = for {
    _ <- q.offer(())
    a <- task
    _ <- q.take
  } yield a
}

object RateLimiter3 {

  def create: UIO[RateLimiter3] = {

//    def take(highPriorityQueue: Queue[Unit], lowPriorityQueue: Queue[Unit]): UIO[Unit] =
//      highPriorityQueue.poll.flatMap {
//        case Some(_) => UIO.unit
//        case None => lowPriorityQueue.poll.map(_ => ())
//      }

    for {
      highPriorityQueue <- Queue.bounded[Unit](1)
      // lowPriorityQueue <- Queue.bounded[Unit](1)
      _ <- highPriorityQueue.take.forever.fork
    } yield new RateLimiter3(highPriorityQueue)
  }
}

class RateLimiter3 private(highPriorityQueue: Queue[Unit]) {

  def rateLimit[A](task: Task[A]): Task[A] =
    highPriorityQueue.offer(()).flatMap(_ => task)
}