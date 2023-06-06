package livechart
import org.scalajs.dom._
import scala.scalajs.js.JSON
import scala.scalajs.js.Thenable.Implicits._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}
import scala.scalajs.js
import io.circe.syntax._
import io.circe.generic.auto._
import com.raquo.laminar.api.L.{*, given}
import scala.concurrent.Future
import org.scalajs.dom.RequestInit

def getTodos(): Future[js.Array[TodoItem]] = {
  fetch("/api/todos")
    .flatMap(_.text().toFuture)
    .map(JSON.parse(_))
    .map(_.asInstanceOf[js.Array[TodoItem]])
}

def createTodo(item: TodoItem): Future[TodoItem] = {

  val headers = new Headers()
  headers.append("Content-Type", "application/json")
  val options = js.Dynamic.literal(
    method = "POST",
    headers = headers,
    body = item.asJson.noSpaces
  )
  val requestInit =
    options.asInstanceOf[RequestInit]

  fetch("/api/todos", requestInit)
    .flatMap(_.text().toFuture)
    .map(JSON.parse(_))
    .map(_.asInstanceOf[TodoItem])
}

def setTodo(item: TodoItem): Future[TodoItem] = {
  val headers = new Headers()
  headers.append("Content-Type", "application/json")
  val options = js.Dynamic.literal(
    method = "PUT",
    headers = headers,
    body = item.asJson.noSpaces
  )
  val requestInit =
    options.asInstanceOf[RequestInit]

  fetch(s"/api/todos/${item.id}", requestInit)
    .flatMap(_.text().toFuture)
    .map(JSON.parse(_))
    .map(_.asInstanceOf[TodoItem])
}

def deleteTodo(id: Int): Future[TodoItem] = {
  val options = js.Dynamic.literal(
    method = "DELETE"
  )
  val requestInit =
    options.asInstanceOf[RequestInit]

  fetch(s"/api/todos/${id}", requestInit)
    .flatMap(_.text().toFuture)
    .map(JSON.parse(_))
    .map(_.asInstanceOf[TodoItem])
}

def deleteCompletedTodos(): Future[js.Array[TodoItem]] = {
  val options = js.Dynamic.literal(
    method = "DELETE"
  )
  val requestInit =
    options.asInstanceOf[RequestInit]

  fetch(s"/api/todos/completed", requestInit)
    .flatMap(_.text().toFuture)
    .map(JSON.parse(_))
    .map(_.asInstanceOf[js.Array[TodoItem]])
}
