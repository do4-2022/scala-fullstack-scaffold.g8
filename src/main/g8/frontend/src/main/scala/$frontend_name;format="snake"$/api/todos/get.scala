package $frontend_name;format="snake"$.api.todos

import org.scalajs.dom._
import scala.concurrent.Future
import scala.scalajs.js.Thenable.Implicits._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import $frontend_name;format="snake"$.state.TodoItem
import $frontend_name;format="snake"$.api.API_URL

def getTodos(): Future[List[TodoItem]] = {
  fetch(s"\${API_URL}/api/todos")
    .flatMap(_.text().toFuture)
    .flatMap(json => {
      decode[List[TodoItem]](json) match {
        case Right(value: List[TodoItem]) => Future.successful(value)
        case Left(error)                  => Future.failed(error)
      }
    })
}
