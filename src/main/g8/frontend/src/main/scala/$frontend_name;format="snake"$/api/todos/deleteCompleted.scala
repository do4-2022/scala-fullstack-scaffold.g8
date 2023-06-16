package $frontend_name;format="snake"$.api.todos

import org.scalajs.dom._
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import $frontend_name;format="snake"$.state.TodoItem
import $frontend_name;format="snake"$.api.API_URL

def deleteCompletedTodos(): Future[String] = {
  val options = js.Dynamic.literal(
    method = "DELETE"
  )
  val requestInit =
    options.asInstanceOf[RequestInit]

  fetch(s"\${API_URL}/api/todos/completed", requestInit)
    .flatMap(_.text().toFuture)
}
