package livechart
import org.scalajs.dom._
import scala.scalajs.js.Thenable.Implicits._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}
import scala.scalajs.js
import io.circe.syntax._
import io.circe.generic.auto._
import com.raquo.laminar.api.L.{given, *}
import scala.concurrent.Future
import org.scalajs.dom.RequestInit
import scala.scalajs.js.JSON
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import scala.scalajs.js.annotation.JSImport

val env_api_url = js.Dynamic.global.window.VITE_API_URL

val API_URL = {

  if (env_api_url != null) {
    env_api_url.asInstanceOf[String]
  } else {
    "http://localhost:3000"
  }
}

def getTodos(): Future[List[TodoItem]] = {
  fetch(s"${API_URL}/api/todos")
    .flatMap(_.text().toFuture)
    .flatMap(json => {
      decode[List[TodoItem]](json) match {
        case Right(value: List[TodoItem]) => Future.successful(value)
        case Left(error)                  => Future.failed(error)
      }
    })
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

  fetch(s"${API_URL}/api/todos", requestInit)
    .flatMap(_.text().toFuture)
    .flatMap(json => {
      decode[TodoItem](json) match {
        case Right(value: TodoItem) => Future.successful(value)
        case Left(error)            => Future.failed(error)
      }
    })
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

  fetch(s"${API_URL}/api/todos/${item.id}", requestInit)
    .flatMap(_.text().toFuture)
    .flatMap(json => {
      decode[TodoItem](json) match {
        case Right(value: TodoItem) => Future.successful(value)
        case Left(error)            => Future.failed(error)
      }
    })
}

def deleteTodo(id: Int): Future[TodoItem] = {
  val options = js.Dynamic.literal(
    method = "DELETE"
  )
  val requestInit =
    options.asInstanceOf[RequestInit]

  fetch(s"${API_URL}/api/todos/${id}", requestInit)
    .flatMap(_.text().toFuture)
    .flatMap(json => {
      decode[TodoItem](json) match {
        case Right(value: TodoItem) => Future.successful(value)
        case Left(error)            => Future.failed(error)
      }
    })
}

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
