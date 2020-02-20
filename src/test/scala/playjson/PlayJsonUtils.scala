package playjson

import play.api.libs.json._
import cats.implicits._

object PlayJsonUtils {

  def readsBy[A, B: Reads](f: B => Either[String, A]): Reads[A] = {
    def read(json: JsValue): Either[String, A] = for {
      b <- json.asOpt[B].toRight("Error reading json data")
      a <- f(b)
    } yield a
    Reads[A](json => read(json).bimap(JsError(_), JsSuccess(_)).merge)
  }

  def writesBy[A, B: Writes](f: A => B): Writes[A] = Writes[A](a => Json.toJson(f(a)))
}
