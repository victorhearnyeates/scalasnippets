package sttp

import java.io.{BufferedOutputStream, File, FileOutputStream}
import java.nio.ByteBuffer
import scala.util.Try

import org.scalatest.{EitherValues, FunSuite, Matchers}
import sttp.SttpTest._
import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client.asynchttpclient.ziostreams.AsyncHttpClientZioStreamsBackend
import zio._
import zio.internal.Platform
import zio.stream.Stream

class SttpTest extends FunSuite with Matchers with EitherValues {

  // https://github.com/loicdescotte/scalaIO-streaming-examples/blob/master/src/main/scala/zio/ZioCirce.scala

  val runtime = Runtime.unsafeFromLayer(ZEnv.live, Platform.default)
  implicit val sttpBackend = runtime.unsafeRun(AsyncHttpClientZioBackend())
  implicit val sttpBackend2 = runtime.unsafeRun(AsyncHttpClientZioStreamsBackend())

  ignore("stuff") {

    val file = new File("/Users/jreddick/tmp/test.jpg")

    val request = basicRequest
      .get(uri"http://bwpl.s3-website-eu-west-1.amazonaws.com/303.jpg")
      .response(asStream[Stream[Throwable, ByteBuffer]])

    val response = runtime.unsafeRun(request.send())
    val s: Stream[Throwable, ByteBuffer] = response.body.getOrElse(fail())

    runtime.unsafeRun(writeDataToFile(file, s))
  }
}

object SttpTest {

  def getFileOutputStream(file: File): Task[BufferedOutputStream] =
    Task.fromTry(Try(new BufferedOutputStream(new FileOutputStream(file))))

  def fileResource(file: File): Managed[Throwable, BufferedOutputStream] =
    Managed.make(getFileOutputStream(file))(os => Task.fromTry(Try(os.close())).either.map(_.swap.map(_ => ()).merge))

  def writeDataToFile(file: File, stream: Stream[Throwable, ByteBuffer]): Task[Unit] =
    fileResource(file).use { os =>
      stream.foreach(data => Task.fromTry(Try(os.write(data.array()))))
    }
}