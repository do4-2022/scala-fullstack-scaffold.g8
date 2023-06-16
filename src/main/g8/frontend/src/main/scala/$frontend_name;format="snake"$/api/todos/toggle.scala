package $frontend_name;format="snake"$.api.todos

import org.scalajs.dom._
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import $frontend_name;format="snake"$.state.TodoItem
import $frontend_name;format="snake"$.api.API_URL

def toggleCompleted(itemId : Int): Future[TodoItem] = {

  val options = js.Dynamic.literal(
    method = "POST",
  )
  val requestInit =
    options.asInstanceOf[RequestInit]

  fetch(s"\${API_URL}/todos/\${itemId}/completed", requestInit)
    .flatMap(_.text().toFuture)
    .flatMap(json => {
      decode[TodoItem](json) match {
        case Right(value: TodoItem) => Future.successful(value)
        case Left(error)            => Future.failed(error)
      }
    })
}
