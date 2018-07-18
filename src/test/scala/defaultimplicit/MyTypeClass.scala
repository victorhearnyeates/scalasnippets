package defaultimplicit

trait MyTypeClass[A] {
  def foo(a: A): Option[String]
}

object MyTypeClass {
  def apply[A](implicit instance: MyTypeClass[A]): MyTypeClass[A] = instance
  implicit val intInstance: MyTypeClass[Int] = i => Some(i.toString)
  implicit def defaultInstance[A]: MyTypeClass[A] = _ => None
}
