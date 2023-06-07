package livechart.api.todos

import org.scalajs.dom._
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import livechart.state.TodoItem
import livechart.api.API_URL

def deleteCompletedTodos(): Future[List[TodoItem]] = {
  val options = js.Dynamic.literal(
    method = "DELETE"
  )
  val requestInit =
    options.asInstanceOf[RequestInit]

  fetch(s"${API_URL}/api/todos/completed", requestInit)
    .flatMap(_.text().toFuture)
    .flatMap(json => {
      decode[List[TodoItem]](json) match {
        case Right(value: List[TodoItem]) => Future.successful(value)
        case Left(error)                  => Future.failed(error)
      }
    })
}
