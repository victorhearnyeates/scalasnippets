package akkastreams

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, MergePreferred, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{FunSuite, Matchers}

case class Parent(id: Int, children: Set[Child])
case class Child(id: Int)

case class DataToProcess(
  parentId: Int,
  childId: Int,
  childCount: Int,
  iteration: Int,
  status: Status = Status.NotProcessed
)

object DataToProcess {

  def success(d: DataToProcess): DataToProcess = d.copy(status = Status.Success)

  def failed(d: DataToProcess, maxRetryCount: Int = 2): DataToProcess =
    if (d.iteration < maxRetryCount) d.copy(status = Status.Retry, iteration = d.iteration + 1)
    else d.copy(status = Status.Failed)
}

sealed trait Status

object Status {
  case object NotProcessed extends Status
  case object Success extends Status
  case object Failed extends Status
  case object Retry extends Status
}

object ParentChild {

  def run[A](runnable: RunnableGraph[Future[A]]): Future[A] = {
    implicit val system: ActorSystem = ActorSystem("ParentChild")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    val result: Future[A] = runnable.run()
    result.onComplete(_ => system.terminate())
    result
  }

  def parent(id: Int, childCount: Int): Parent =
    Parent(id, children = (1 to childCount).map(i => Child((id * 10) + i)).toSet)

  val p1 = parent(1, 3)
  val p2 = parent(2, 2)
  val p3 = parent(3, 1)
  val parents: List[Parent] = List(p1, p2, p3)
  val expectedElements: Int = parents.flatMap(_.children.toList).size

  def process(d: DataToProcess): DataToProcess = {
    def even(i: Int): Boolean = (i % 2) == 0
    val failure: Boolean = even(d.childId) && (d.iteration < 2)
    def log(status: String): Unit = println(s"Processing [${d.toString}] - $status")
    if (failure) {
      log("FAILED")
      DataToProcess.failed(d)
    }
    else {
      log("SUCCESS")
      DataToProcess.success(d)
    }
    log("SUCCESS")
    DataToProcess.success(d)
  }

  val source: Source[Parent, NotUsed] = Source(parents)

  val flatten: Flow[Parent, DataToProcess, NotUsed] = Flow[Parent].mapConcat[DataToProcess] {
    p => p.children.map(c => DataToProcess(p.id, c.id, p.children.size, 1))
  }

  val processor: Flow[DataToProcess, DataToProcess, NotUsed] = Flow.fromFunction(process)

  val completed: Flow[DataToProcess, DataToProcess, NotUsed] = {
    Flow[DataToProcess].filter(d => Set[Status](Status.Success, Status.Failed).contains(d.status)).take(expectedElements)
  }

  val retry: Flow[DataToProcess, DataToProcess, NotUsed] = Flow[DataToProcess].filter(_.status == Status.Retry)

  val runnable: RunnableGraph[Future[Seq[DataToProcess]]] =
    RunnableGraph.fromGraph(GraphDSL.create(Sink.seq[DataToProcess]) { implicit b => sink =>
      import GraphDSL.Implicits._
      val broadcast = b.add(Broadcast[DataToProcess](2))
      val merge = b.add(MergePreferred[DataToProcess](1))
      source ~> flatten ~> merge ~> processor ~> broadcast ~> completed.take(expectedElements) ~> sink
      merge.preferred <~ retry <~ broadcast
      ClosedShape
    })
}

class ParentChildTest extends FunSuite with Matchers with ScalaFutures {

  implicit override val patienceConfig = PatienceConfig(timeout = Span(10, Seconds))

  test("Parent factory") {
    val expected = Parent(1, Set(Child(11), Child(12), Child(13)))
    ParentChild.parent(1, 3) should be (expected)
  }

  test("Run Stream") {
    val processed: Seq[DataToProcess] = ParentChild.run[Seq[DataToProcess]](ParentChild.runnable).futureValue
    processed should have size ParentChild.expectedElements
  }
}
