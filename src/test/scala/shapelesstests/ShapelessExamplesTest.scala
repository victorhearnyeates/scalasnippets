package shapelesstests

import scala.collection.immutable.Seq
import org.scalatest.{FunSuite, Matchers}

class ShapelessExamplesTest extends FunSuite with Matchers {

  // https://gist.github.com/fancellu/9a1aafd70b5c316b14f9

  import shapeless._

  test("select coproduct") {

    sealed trait Foo
    final case class Foo1(i: Int) extends Foo
    final case class Foo2(s: String) extends Foo
    final case class Foo3(b: Boolean) extends Foo

    val r1: Option[Foo1] = Generic[Foo].to(Foo1(123)).select[Foo1]
    r1 should contain (Foo1(123))

    val r2: Option[Foo2] = Generic[Foo].to(Foo1(123)).select[Foo2]
    r2 shouldBe empty
  }

  test("LabelledGeneric") {

    import record._, syntax.singleton._

    case class Book(author: String, title: String, id: Int, price: Double)

    val bookGen = LabelledGeneric[Book]
    val book1 = Book("Benjamin Pierce", "Types and Programming Languages", 262162091, 44.11)
    val rec = bookGen.to(book1)

    val price: Double = rec(Symbol("price"))
    price shouldEqual 44.11

    val book2: Book = bookGen.from(rec.updateWith(Symbol("price"))(_ + 2.0))
    book2 shouldEqual book1.copy(price = 46.11)

    case class ExtendedBook(author: String, title: String, id: Int, price: Double, inPrint: Boolean)

    val bookExtGen = LabelledGeneric[ExtendedBook]
    val book3: ExtendedBook = bookExtGen.from(rec + (Symbol("inPrint") ->> true))
    book3 shouldEqual ExtendedBook("Benjamin Pierce", "Types and Programming Languages", 262162091, 44.11, true)
  }

  test("Flatten") {

    import poly._

    val list1 = (23 :: "foo" :: HNil) :: HNil :: (true :: HNil) :: HNil
    val list2 = list1.flatMap(identity)
    list2 shouldEqual 23 :: "foo" :: true :: HNil
  }

  /*
  test("Zipper") {

    import syntax.zipper._

    val l1 = 1 :: "foo" :: 3.0 :: HNil
    val zipper = l1.toZipper

    val r1: Int :: String :: String :: Double :: HNil = zipper.right.insert("A").reify
    r1 shouldEqual 1 :: "A" :: "foo" :: 3.0 :: HNil

    val r2: Int :: String :: Double :: HNil = zipper.last.left.put(123.0).reify
    r2 shouldEqual 1 :: "foo" :: 123.0 :: HNil
  }
  */

  test("Heterogeneous Maps") {

    class BiMapIS[K, V]
    implicit val intToString = new BiMapIS[Int, String]
    implicit val stringToInt = new BiMapIS[String, Int]

    val hm = HMap[BiMapIS](23 -> "foo", "bar" -> 13)

    val r1: Option[String] = hm.get(23)
    r1 should contain ("foo")

    val r2: Option[Int] = hm.get("bar")
    r2 should contain (13)
  }

  test("Records") {

    import shapeless._ ; import syntax.singleton._ ; import record._

    val book =
      ("author" ->> "Benjamin Pierce") ::
      ("title"  ->> "Types and Programming Languages") ::
      ("id"     ->>  262162091) ::
      ("price"  ->>  44.11) ::
      HNil

    val r1: String = book("title")
    r1 shouldEqual "Types and Programming Languages"

    val keys: List[String] = book.keys.toList
    keys shouldEqual List("author", "title", "id", "price")

    case class Book(author: String, title: String, id: Int, price: Double)
    val b: Book = Generic[Book].from(book.values)
    b shouldEqual Book("Benjamin Pierce", "Types and Programming Languages", 262162091, 44.11)
  }

  test("CSV") {

    def row(cols: Seq[String]): String = cols.mkString("\"", "\",\"", "\"")

    def csv[N <: Nat](hdrs : Sized[Seq[String], N], rows : List[Sized[Seq[String], N]]): List[String] =
      row(hdrs) :: rows.map(row(_))

    val hdrs = Sized("Title", "Author")

    val rows = List(
      Sized("Types and Programming Languages", "Benjamin Pierce"),
      Sized("The Implementation of Functional Programming Languages", "Simon Peyton-Jones")
    )

    val formatted: List[String] = csv(hdrs, rows)

    formatted shouldEqual List(
      "\"Title\",\"Author\"",
      "\"Types and Programming Languages\",\"Benjamin Pierce\"",
      "\"The Implementation of Functional Programming Languages\",\"Simon Peyton-Jones\""
    )
  }
}